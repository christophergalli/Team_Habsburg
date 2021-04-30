package ch.fhnw.medicalinformatics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("geneservice")
public class GeneController {

	@Autowired
	private GeneService geneService;
	private static final Logger LOG = LoggerFactory.getLogger(GeneController.class);
	@GetMapping("/byid")
	public Gene getById(@RequestParam(value = "id") Integer id) {
		LOG.debug("Du hast das Gen mit der id " + id + " aufgerufen.");
		Gene gene = geneService.findById(id);
		return gene;
	}
	
	@GetMapping("/bysymbol")
	public List<Gene> getBySymbol(@RequestParam(value = "symbol") String symbol) {
		LOG.debug("Du hast das Symbol  " + symbol + " aufgerufen.");
		List<Gene> genes = geneService.findBySymbol(symbol);
		return genes;
	}

	@GetMapping("/bydescription")
	public List<Gene> getByDescription(@RequestParam(value = "description") String description) {
		LOG.debug("Folgende Gene enthalten die Beschreibung " + description);
		List<Gene> genes = geneService.findByDescription(description);
		return genes;
	}
	/*
	@GetMapping("/test")
	public Gene getTestGene() {
		Gene g = new Gene();
		g.setGeneId(1234);
		return g;
	}
	 */
}
