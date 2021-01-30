#!groovy
pipeline {
	agent any
	environment {
		// 发布脚本目录
		DEPLOY_SCRIPT_DIR = "/usr/local/software/jenkins/deploy_script_dir"
		// 项目路径
		PROJECT_DIR = "${pwd()}/${params.project_name}"
		// 获取本项目正在使用的docker镜像id。""里面的\$表示字面值$
		OLD_IMAGES_ID = sh(script: "docker images | grep ${params.project_name} | awk '{print \$3}'", returnStdout: true).trim()
		// 声明devops自定义配置变量
		APP_DEVOPS_JSON = ""
	}	
	stages {
		stage('拉取devops配置') {
			steps {
				echo "【信息】jenkins配置参数 ${params}"

				// 拉取配置文件
				dir("${DEPLOY_SCRIPT_DIR}"){
					git "${GIT_URL}"
				}
				sh "chmod a+x ${DEPLOY_SCRIPT_DIR}/java-test/run.sh"
			}
		}
		stage('克隆项目代码') {	
			steps {
				// 通过 Pipeline Syntax 生成语法
				git branch: "${params.git_branch}", credentialsId: '96df8f2c-eee0-456b-8d80-d70c18906b92', url: "${params.git_url}"

				script {
					// 单个项目的git工程，项目路径就是当前路径
					PROJECT_DIR = fileExists(PROJECT_DIR) ? PROJECT_DIR : "${pwd()}"

				    initAppDevopsMap(PROJECT_DIR)
				}

				// Dockerfile替换变量，${变量 ?: ''}如果变量不存在就返回''
				sh "sed -i \"s,{{appServerPort}},${APP_DEVOPS_JSON.appServerPort ?: ''},g\" ${DEPLOY_SCRIPT_DIR}/java-test/Dockerfile"
				// run.sh替换变量
				sh "sed -i \"s,{{jvmOptions}},${APP_DEVOPS_JSON.jvmOptions ?: ''},g;s,{{programArguments}},${APP_DEVOPS_JSON.programArguments ?: ''},g\" ${DEPLOY_SCRIPT_DIR}/java-test/run.sh"
				
			}
		}
		stage('编译代码') {
			steps {
				sh "/usr/local/software/apache-maven-3.6.0/bin/mvn clean package -Dmaven.test.skip=true"
			}
		}
		stage('构建Docker镜像') {
			steps {
				// 拷贝docker打包文件、jar包到docker-image
				sh "mkdir ${PROJECT_DIR}/target/docker-image"
				sh "cp ${DEPLOY_SCRIPT_DIR}/java-test/* ${PROJECT_DIR}/target/docker-image/"
				sh "cp ${PROJECT_DIR}/target/*.jar ${PROJECT_DIR}/target/docker-image/"
				
				// 构建镜像
				dir("${PROJECT_DIR}/target/docker-image"){
					sh "docker build -t ${params.project_name} ."
				}
			}
		}
		stage('运行Docker容器') {
			steps {
				sh "docker rm -f ${params.project_name} || true"
				sh "docker run -d -p ${APP_DEVOPS_JSON.appServerPort ?: ''}:${APP_DEVOPS_JSON.appServerPort ?: ''} --name=${params.project_name} -m 600m  ${params.project_name}"
				sh "docker rmi ${OLD_IMAGES_ID} || true"
			}
		}		
	}	
}


// 字符串是否为空
def stringIsBlank(str){
    str == null || "" == str.trim()
}

/**
 * 需要去Available下安装 pipeline utility steps
 * 读取app-devops.yml赋值给APP_DEVOPS_JSON
 */
def initAppDevopsMap(projectDir){
	def appDevops = readYaml file:"${projectDir}/app-devops.yml"
	APP_DEVOPS_JSON = params.env == "dev" 
		? appDevops.dev : params.env == "test" 
		? appDevops.test : params.env == "uat" 
		? appDevops.uat : params.env == "prod" 
		? appDevops.prod : ""
	if (APP_DEVOPS_JSON == "") {
		println "【错误】通过流水线env变量读取app-devops.yml的结果为空"
	}
	println "【信息】APP_DEVOPS_JSON ${APP_DEVOPS_JSON}"
}


