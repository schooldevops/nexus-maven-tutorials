# Nexus 설치 및 Maven 의존성 관리하기 

[01. sonatype/nexus3 설치하고 리포지토리 생성하기](./01.nexus3_docker_hub.md)

[02. Nexus에 Spring Maven 라이브러리 배포 및 사용하기](./02.nexus3_spring_usage.md)

[03. Kubernetes 에 Nexus 설치하기. ](./InstallNexus/README.md)

## Java Library 생성을 위한 설정 

- Java Library를 생성하기 위해서 다음 내용만 기술할 것이다. 
- settings.xml
- pom.xml
- maven command
### Settings.xml 설정하기. 

- ~/.m2/settings.xml 파일을 생성한다. 

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
   http://maven.apache.org/xsd/settings-1.0.0.xsd">

    <servers>
        <server>
            <id>test-snapshots</id>
            <username>test_guest</username>
            <password>test1234</password>
        </server>
        <server>
            <id>test-releases</id>
            <username>test_guest</username>
            <password>test1234</password>
        </server>
    </servers>
</settings>

```

### pom.xml 파일 지정하기. 

- pom.xml 파일중 다음 내용을 추가한다. 

```xml
... 생략 

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

	<distributionManagement>
		<repository>
			<id>test-releases</id>
			<url>http://localhost:32000/repository/test-releases/</url>
		</repository>
		<snapshotRepository>
			<id>test-snapshots</id>
			<url>http://localhost:32000/repository/test-snapshots/</url>
		</snapshotRepository>
	</distributionManagement>
</project>
```

- 위와 같이 settings.xml 에 지정한 서버 id와 repository의 id값을 통일 시킨다. 
- 그리고 url은 nexus 서버에 생성한 repository url을 지정한다. 

### maven 디플로이하기. 

```
mvn clean deploy
```

- 위와 같이 `~/.m2/settings.xml` 를 이용한다면 위와 같이 커맨드를 이용하면 된다. 
  
```
mvn clean deploy -s settings.xml 
```

- 만약 직접 settings.xml 을 별도로 지정한다면 위와 같이 커맨드를 이용하면 된다. 


