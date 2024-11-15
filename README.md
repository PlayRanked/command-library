# Comamnd Library
An insanely useful command library with tab completion and advanced validation.

### How it works
Learn the features and usage on our [Wiki](../../wiki)

### Repository
Maven
```xml
<repositories>
  <repository>
    <id>playranked-public</id>
    <url>https://nexus.playranked.net/repository/public/</url>
  </repository>
</repositories>

<dependency>
  <groupId>net.playranked.library</groupId>
  <artifactId>command-library</artifactId>
  <version>1.0</version>
</dependency>  
```
Gradle
```groovy
maven {
    url = "https://nexus.playranked.net/repository/public/"
}

implementation 'net.playranked.library:command-library:1.0'
```
