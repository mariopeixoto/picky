## Picky: Lightweight Regression Test Selection for Java Applications


### How to use (check sample project included as an example)
 
1. Download code
2. Run mvn install on root
3. Add the following to your pom.xml

```xml
<build>
  ...
  <plugin>
      <groupId>com.picky</groupId>
      <artifactId>
        picky-maven-plugin
      </artifactId>
      <version>0.1-SNAPSHOT</version>
      <executions>
          <execution>
              <id>picky</id>
              <goals>
                  <goal>select</goal>
                  <goal>collect</goal>
              </goals>
          </execution>
      </executions>
  </plugin>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.19.1</version>
    <configuration>
        <excludesFile>${project.basedir}/excludedTests.txt</excludesFile>
    </configuration>
  </plugin>
  ...
</build>

<dependencies>
  ...
  <dependency>
    <groupId>com.picky</groupId>
    <artifactId>picky-maven-plugin</artifactId>
    <version>0.1</version>
  </dependency>
  ...
</dependencies>
```

4. Create excludedTests.txt on your root if you don't have it
5. Run mvn test on your project