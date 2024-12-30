package hr.foi.servicesync.controllers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["hr.foi.servicesync"])
class ControllersApplication

fun main(args: Array<String>) {
	runApplication<ControllersApplication>(*args)
}
