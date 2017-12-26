# scala-egg-server
Scala backend for [It Is The Egg](https://github.com/danieljharvey/it-is-the-egg)

Very much a work in progress and learning exercise for Scala - plan is to replace the PHP backend from that project with this.

## DB settings ##

You will need to copy `/src/main/resources/application.properties.example` to `application.properties` and add your local DB credentials to get things up and running.

## Build & Run ##

```sh
$ cd Egg_Server
$ ./sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.
