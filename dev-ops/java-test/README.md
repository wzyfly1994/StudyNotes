开发环境 192.168.4.202 pipeline script

jenkins配置：
	1、新建流水线
	2、General
		2.1、丢弃旧的构建
		2.2、参数化构建过程
			字符串参数
				Name：git_url
				Default Value：项目git地址
			Git parameter
				Name：git_branch
				Parameter Type：Branch
				Default Value：develop
				Branch Filter：origin/(.*)
				Tag Filter：*
				Selected Value：DEFAULT
				Use repository：再次填写项目git地址
			选项参数
				Name：project_name
				Choices：项目名称
	3、Pipeline
		Definition：Pipeline script from SCM
		SCM： Git
			Repositories： devops脚本的git地址
			Credentials：账号密码证书
			Branches to build：*/master
			Script Path：java/Jenkinsfile