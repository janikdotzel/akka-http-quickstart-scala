package io.janikdotzel.url

import com.typesafe.config._


object LibraryConfig {
  // Either load your config like you normally would
  val fileBasedConfig: Config = ConfigFactory.load("library") // Replace "library" with the actual name of your configuration file

  // Or define it in code
  val codeBasedConfig: Config = ConfigFactory.parseString("akka.http.parsing.max-uri-length = 15000")



}
