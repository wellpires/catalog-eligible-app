package com.catalog.eligibleads.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalog.eligibleads.controller.resource.PingResource;

@RestController
public class PingController implements PingResource {

	@Override
	@GetMapping(path = "ping")
	public ResponseEntity<String> ping() {

		List<String> phrases = Arrays.asList("Charlie Brown Jr é a pior coisa que eu já ouvi!",
				"Legião Urbana é muito chato!", "Eu vou juntar uma galera!", "Quando eu cheguei estava tudo zoneado!",
				"Hoje é dia de UCL Off!", "Perdemos, mas perdemos por pouco!",
				"Competimos em equipe, para ganhar separado!", "Hoje tem evento especial?!",
				"Levei um tecão na cara mané!", "Desculpa ae mano!",
				"Estou aqui para somar, não para fazer a diferença", "Esse minino é muito fofo!", "Um xabu por vez",
				"1o de Março", "No shopping hoje não", "Mas é um elefante branco mesmo!",
				"Você parece um breakpoint, a conversa sempre pára em você",
				"Vamos colocar na lista das coisas que nunca iremos fazer", "Corporative Card!",
				"Parabéns Robshow! UHUUULLL!", "Você é bobo cara!");

		String phrase = phrases.get(RandomUtils.nextInt(0, phrases.size()));
		return ResponseEntity.ok(phrase);
	}

}
