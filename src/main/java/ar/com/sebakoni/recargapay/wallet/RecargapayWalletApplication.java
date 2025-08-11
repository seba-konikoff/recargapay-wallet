package ar.com.sebakoni.recargapay.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = { "ar.com.sebakoni.recargapay.wallet.entities" })
public class RecargapayWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecargapayWalletApplication.class, args);
	}

}
