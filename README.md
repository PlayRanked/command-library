# Comamnd Library
An insanely useful command library with tab completion and advanced validation.

### How it works
Learn the features and usage on our [Wiki](../../wiki)

### Repository
Maven
```xml
<repositories>
  <repository>
    <id>trinsic-public</id>
    <url>https://repo.trinsic.dev/repository/minecraft-public/</url>
  </repository>
</repositories>

<dependency>
  <groupId>dev.trinsic.library</groupId>
  <artifactId>command-library</artifactId>
  <version>1.0</version>
</dependency>  
```
Gradle
```groovy
maven {
    url = "https://repo.trinsic.dev/repository/public/"
}

implementation 'dev.trinsic.library:command-library:1.0'
```
