package ch.fhnw.medicalinformatics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("geneservice")
public class GeneController {

	@Autowired
	private GeneService geneService;
	
	@GetMapping("/byid")
	public Gene getById(@RequestParam(value = "id") Integer id) {
		Gene gene = geneService.findById(id);
		return gene;
	}
	
	@GetMapping("/bysymbol")
	public List<Gene> getBySymbol(@RequestParam(value = "symbol") String symbol) {
		List<Gene> genes = geneService.findBySymbol(symbol);
		return genes;
	}

	@GetMapping("/bydescription")
	public List<Gene> getByDescription(@RequestParam(value = "description") String description) {
		List<Gene> genes = geneService.findByDescription(description);
		return genes;
	}
	@GetMapping("/test")
	public Gene getTestGene() {
		Gene g = new Gene();
		g.setGeneId(1234);
		return g;
	}
}
