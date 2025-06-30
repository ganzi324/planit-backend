package com.planit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PlanitBackendApplication

fun main(args: Array<String>) {
	runApplication<PlanitBackendApplication>(*args)
}
