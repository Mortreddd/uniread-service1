package com.bsit.uniread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This project is most likely similar to wattpad, but the key difference is, it allows collaborations
 * to other users with one book creations. It also supports the Realtime Messaging to other users.
 * It's either group message for the collaborator, via followers, via friend, etc. I'm hoping this project will
 * be enough to land me a job XD. That's all.
 * author - Emmanuel Male December 1, 2024
 */
@SpringBootApplication
@EnableTransactionManagement
public class UnireadApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnireadApplication.class, args);
	}

}
