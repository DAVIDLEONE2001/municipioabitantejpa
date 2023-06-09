package it.prova.municipioabitantejpa.test;

import java.util.List;

import org.hibernate.LazyInitializationException;

import it.prova.municipioabitantejpa.dao.EntityManagerUtil;
import it.prova.municipioabitantejpa.model.Abitante;
import it.prova.municipioabitantejpa.model.Municipio;
import it.prova.municipioabitantejpa.service.MyServiceFactory;
import it.prova.municipioabitantejpa.service.abitante.AbitanteService;
import it.prova.municipioabitantejpa.service.municipio.MunicipioService;

public class TestMunicipioAbitante {

	public static void main(String[] args) {

		MunicipioService municipioService = MyServiceFactory.getMunicipioServiceInstance();
		AbitanteService abitanteService = MyServiceFactory.getAbitanteServiceInstance();

		try {

			// ora con il service posso fare tutte le invocazioni che mi servono
//			System.out.println(
//					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");
//
//			testInserisciMunicipio(municipioService);
//			System.out.println(
//					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");
//
//			testInserisciAbitante(municipioService, abitanteService);
//			System.out.println(
//					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");
//
//			testRimozioneAbitante(municipioService, abitanteService);
//			System.out.println(
//					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");
//
//			testCercaTuttiGliAbitantiConNome(municipioService, abitanteService);
//			System.out.println(
//					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");
//
//			testLazyInitExc(municipioService, abitanteService);
//			testFindAllByAbitantiMinorenni(municipioService, abitanteService );
//			testFindAllByDescrizioneIniziaCon(municipioService, abitanteService);
//			testFindAllByCodiceMunicipioIniziaCon(municipioService, abitanteService);

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// questa è necessaria per chiudere tutte le connessioni quindi rilasciare il
			// main
			EntityManagerUtil.shutdown();
		}

	}

	private static void testInserisciMunicipio(MunicipioService municipioService) throws Exception {
		System.out.println(".......testInserisciMunicipio inizio.............");
		// creo nuovo municipio
		Municipio nuovoMunicipio = new Municipio("Municipio III", "III", "Via dei Nani");
		if (nuovoMunicipio.getId() != null)
			throw new RuntimeException("testInserisciMunicipio fallito: record già presente ");

		// salvo
		municipioService.inserisciNuovo(nuovoMunicipio);
		// da questa riga in poi il record, se correttamente inserito, ha un nuovo id
		// (NOVITA' RISPETTO AL PASSATO!!!)
		if (nuovoMunicipio.getId() == null)
			throw new RuntimeException("testInserisciMunicipio fallito ");

		System.out.println(".......testInserisciMunicipio fine: PASSED.............");
	}

	private static void testInserisciAbitante(MunicipioService municipioService, AbitanteService abitanteService)
			throws Exception {
		System.out.println(".......testInserisciAbitante inizio.............");

		// creo nuovo abitante ma prima mi serve un municipio
		List<Municipio> listaMunicipiPresenti = municipioService.listAllMunicipi();
		if (listaMunicipiPresenti.isEmpty())
			throw new RuntimeException("testInserisciAbitante fallito: non ci sono municipi a cui collegarci ");

		Abitante nuovoAbitante = new Abitante("Pluto", "Plutorum", 77, "Via Lecce");
		// lo lego al primo municipio che trovo
		nuovoAbitante.setMunicipio(listaMunicipiPresenti.get(0));

		// salvo il nuovo abitante
		abitanteService.inserisciNuovo(nuovoAbitante);

		// da questa riga in poi il record, se correttamente inserito, ha un nuovo id
		// (NOVITA' RISPETTO AL PASSATO!!!)
		if (nuovoAbitante.getId() == null)
			throw new RuntimeException("testInserisciAbitante fallito ");

		// il test fallisce anche se non è riuscito a legare i due oggetti
		if (nuovoAbitante.getMunicipio() == null)
			throw new RuntimeException("testInserisciAbitante fallito: non ha collegato il municipio ");

		System.out.println(".......testInserisciAbitante fine: PASSED.............");
	}

	private static void testRimozioneAbitante(MunicipioService municipioService, AbitanteService abitanteService)
			throws Exception {
		System.out.println(".......testRimozioneAbitante inizio.............");

		// inserisco un abitante che rimuoverò
		// creo nuovo abitante ma prima mi serve un municipio
		List<Municipio> listaMunicipiPresenti = municipioService.listAllMunicipi();
		if (listaMunicipiPresenti.isEmpty())
			throw new RuntimeException("testRimozioneAbitante fallito: non ci sono municipi a cui collegarci ");

		Abitante nuovoAbitante = new Abitante("Pietro", "Mitraglia", 33, "Via del Mare");
		// lo lego al primo municipio che trovo
		nuovoAbitante.setMunicipio(listaMunicipiPresenti.get(0));

		// salvo il nuovo abitante
		abitanteService.inserisciNuovo(nuovoAbitante);

		Long idAbitanteInserito = nuovoAbitante.getId();
		abitanteService.rimuovi(idAbitanteInserito);
		// proviamo a vedere se è stato rimosso
		if (abitanteService.caricaSingoloAbitante(idAbitanteInserito) != null)
			throw new RuntimeException("testRimozioneAbitante fallito: record non cancellato ");
		System.out.println(".......testRimozioneAbitante fine: PASSED.............");
	}

	private static void testCercaTuttiGliAbitantiConNome(MunicipioService municipioService,
			AbitanteService abitanteService) throws Exception {
		System.out.println(".......testCercaTuttiGliAbitantiConNome inizio.............");

		// inserisco un paio di abitanti di test
		// prima mi serve un municipio
		List<Municipio> listaMunicipiPresenti = municipioService.listAllMunicipi();
		if (listaMunicipiPresenti.isEmpty())
			throw new RuntimeException(
					"testCercaTuttiGliAbitantiConNome fallito: non ci sono municipi a cui collegarci ");

		Abitante nuovoAbitante = new Abitante("Mariotto", "Bassi", 27, "Via Lucca");
		Abitante nuovoAbitante2 = new Abitante("Mariotto", "Nato", 37, "Via Roma");
		// lo lego al primo municipio che trovo
		nuovoAbitante.setMunicipio(listaMunicipiPresenti.get(0));
		nuovoAbitante2.setMunicipio(listaMunicipiPresenti.get(0));

		// salvo i nuovi abitante
		abitanteService.inserisciNuovo(nuovoAbitante);
		abitanteService.inserisciNuovo(nuovoAbitante2);

		// ora mi aspetto due 'Mario'
		if (abitanteService.cercaTuttiGliAbitantiConNome("Mariotto").size() != 2)
			throw new RuntimeException("testCercaTuttiGliAbitantiConNome fallito: numero record inatteso ");

		// clean up code
		abitanteService.rimuovi(nuovoAbitante.getId());
		abitanteService.rimuovi(nuovoAbitante2.getId());

		System.out.println(".......testCercaTuttiGliAbitantiConNome fine: PASSED.............");
	}

	private static void testLazyInitExc(MunicipioService municipioService, AbitanteService abitanteService)
			throws Exception {
		System.out.println(".......testLazyInitExc inizio.............");

		// prima mi serve un municipio
		List<Municipio> listaMunicipiPresenti = municipioService.listAllMunicipi();
		if (listaMunicipiPresenti.isEmpty())
			throw new RuntimeException("testLazyInitExc fallito: non ci sono municipi a cui collegarci ");

		Municipio municipioSuCuiFareIlTest = listaMunicipiPresenti.get(0);
		// se interrogo la relazione devo ottenere un'eccezione visto che sono LAZY
		try {
			municipioSuCuiFareIlTest.getAbitanti().size();
			// se la riga sovrastante non da eccezione il test fallisce
			throw new RuntimeException("testLazyInitExc fallito: eccezione non lanciata ");
		} catch (LazyInitializationException e) {
			// 'spengo' l'eccezione per il buon fine del test
		}
		// una LazyInitializationException in quanto il contesto di persistenza è chiuso
		// se usiamo un caricamento EAGER risolviamo...dipende da cosa ci serve!!!
		// municipioService.caricaSingoloMunicipioConAbitanti(...);
		System.out.println(".......testLazyInitExc fine: PASSED.............");
	}

	private static void testFindAllByAbitantiMinorenni(MunicipioService municipioService,
			AbitanteService abitanteService) throws Exception {
		System.out.println(".......testCercaTuttiGliAbitantiConNome inizio.............");
		
		List<Municipio> listaMunicipiPresenti = municipioService.listAllMunicipi();
		if (listaMunicipiPresenti.isEmpty())
			throw new RuntimeException(
					"testFindAllByAbitantiMinorenni fallito: non ci sono municipi a cui collegarci ");

		Abitante nuovoAbitante = new Abitante("Mariotto", "Bassi", 17, "Via Lucca");
		Abitante nuovoAbitante2 = new Abitante("Mariotto", "Nato", 17, "Via Roma");
		// lo lego al primo municipio che trovo
		nuovoAbitante.setMunicipio(listaMunicipiPresenti.get(0));
		nuovoAbitante2.setMunicipio(listaMunicipiPresenti.get(0));

		// salvo i nuovi abitante
		abitanteService.inserisciNuovo(nuovoAbitante);
		abitanteService.inserisciNuovo(nuovoAbitante2);

		List<Municipio> minorenni = municipioService.cercaTuttiIMunicipiConMinorenni();
		System.out.println(minorenni);
		
		abitanteService.rimuovi(nuovoAbitante.getId());
		abitanteService.rimuovi(nuovoAbitante2.getId());
		
		if (minorenni.size()<1) {
			throw new RuntimeException("testFindAllByAbitantiMinorenni fallito: eccezione non lanciata ");

		}

		System.out.println(".......testFindAllByAbitantiMinorenni fine: PASSED.............");
	}

	private static void testFindAllByDescrizioneIniziaCon(MunicipioService municipioService,
			AbitanteService abitanteService) throws Exception {
		System.out.println(".......testFindAllByDescrizioneIniziaCon inizio.............");

		List<Municipio> municipiDaMetodo = municipioService.cercaTuttiIMunicipiConDescrizioneIniziaCon("mu");
		
		if (municipiDaMetodo.size()<1) {
			throw new RuntimeException("testFindAllByDescrizioneIniziaCon fallito: eccezione non lanciata ");

		}
		System.out.println(".......testFindAllByDescrizioneIniziaCon fine: PASSED.............");
		
		

		System.out.println(".......testCercaTuttiGliAbitantiConNome fine: PASSED.............");
	}
	
	private static void testFindAllByCodiceMunicipioIniziaCon(MunicipioService municipioService,
			AbitanteService abitanteService) throws Exception {
		System.out.println(".......testFindAllByDescrizioneIniziaCon inizio.............");

		List<Abitante> abitantiDaMetodo = abitanteService.cercaTuttiGliAbitantiConCodiceMunicipioIniziaCon("I");
		
		if (abitantiDaMetodo.size()<1) {
			throw new RuntimeException("testFindAllByDescrizioneIniziaCon fallito: eccezione non lanciata ");

		}
		System.out.println(".......testFindAllByDescrizioneIniziaCon fine: PASSED.............");
		
		

		System.out.println(".......testCercaTuttiGliAbitantiConNome fine: PASSED.............");
	}

}
