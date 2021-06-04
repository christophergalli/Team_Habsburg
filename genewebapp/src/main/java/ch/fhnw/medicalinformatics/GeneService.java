package ch.fhnw.medicalinformatics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.NamedEvent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@NamedEvent
@ApplicationScoped
@ManagedBean(name = "geneService")
public class GeneService {
	private static final Logger logger = LoggerFactory.getLogger(GeneService.class);

	private String searchOption;
	private String searchTerm;
	private List<Gene> data = new ArrayList<>();
	private String hostname = "http://localhost:8080";

	private final OkHttpClient httpClient;


	public GeneService() {
		httpClient = new OkHttpClient.Builder()
			      .connectTimeout(180, TimeUnit.SECONDS)
			      .readTimeout(180, TimeUnit.SECONDS)
			      .writeTimeout(180, TimeUnit.SECONDS)
			      .build();
	}

	public List<String> getSearchOptions() {
		List<String> result = new ArrayList<String>();
		result.add("Search by ID");
		result.add("Search by Symbol");
		result.add("search by Description");
		logger.info("appended a new search");
		return result;
	}

	public String getSearchOption() {
		return searchOption;
	}

	public void setSearchOption(String searchOption) {
		this.searchOption = searchOption;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public void retrieveData() {
		logger.info("retrieve data");
		data.clear();
		// retrieve data from the service
		Request request = null;
		String serviceCall = "";
		if (searchOption.toUpperCase().contains("ID")) {
			serviceCall = "/geneservice/byid?id=" + searchTerm;
		} else if (searchOption.toUpperCase().contains("SYMBOL")) {
			serviceCall = "/geneservice/bysymbol?symbol=" + searchTerm;
		} else if (searchOption.toUpperCase().contains("DESCRIPTION")) {
			serviceCall = "/geneservice/bydescription?description=" + searchTerm;
		} else {
			logger.info("invalid search");
			// TODO: Exception Handling
		}

		logger.info("URL: " + hostname + serviceCall);

		request = new Request.Builder().url(hostname + serviceCall).build();

		try (Response response = httpClient.newCall(request).execute()) {
			Gson g = new Gson();
			Type resultType = null;
			if (searchOption.toUpperCase().contains("ID")) {
				resultType = new TypeToken<Gene>() {}.getType();
				Gene sg = g.fromJson(response.body().string(), resultType);
				data.add(sg);
			} else {
				resultType = new TypeToken<List<Gene>>() {}.getType();
				data = g.fromJson(response.body().string(), resultType);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List<Gene> getGenes() {
		return data;
	}
}
