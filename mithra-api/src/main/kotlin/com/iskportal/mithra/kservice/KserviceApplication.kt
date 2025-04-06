package com.iskportal.mithra.kservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(
	scanBasePackages = [
		"com.iskportal.mithra.kservice",
		"com.iskportal.mithra.exception",
		"com.iskportal.kauth"
	]
)
@ConfigurationPropertiesScan
class KserviceApplication

fun main(args: Array<String>) {
	runApplication<KserviceApplication>(*args)
}
