package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private MeteoDAO dao=new MeteoDAO();
	private int costMin;
	List<Rilevamento>soluzione= new ArrayList<>();

	public Model() {

	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		
		
		List<Rilevamento> rT = dao.getAllRilevamentiLocalitaMese(mese, "Torino");
		List<Rilevamento> rM = dao.getAllRilevamentiLocalitaMese(mese, "Milano");
		List<Rilevamento> rG = dao.getAllRilevamentiLocalitaMese(mese, "Genova");
		String str="";
		int sumT=0;
		int sumM=0;
		int sumG=0;
		
		for(Rilevamento t: rT) {
			sumT+=t.getUmidita();
		}
		double mT=sumT/rT.size();
		
		for(Rilevamento m: rM) {
			sumM+=m.getUmidita();
		}
		double mM=sumM/rM.size();
		
		
		for(Rilevamento g: rG) {
			sumG+=g.getUmidita();
		}
		double mG= sumG/rG.size();
		
		str="Torino: "+mT+"\n"+"Milano: "+mM+"\n"+"Genova: "+mG+"\n";
		return str;
	}

	
	public String trovaSequenza(int mese) {
		String out = "";
		this.costMin = Integer.MAX_VALUE;
		recursive(new ArrayList<Rilevamento>(), mese);
		
		
		for(Rilevamento r: soluzione) {
			out+=r.toString()+"\n";
		}
		out += "Costo totale: " + this.costMin;
		return out;
	}

	
	private void recursive(List<Rilevamento> parziale, int mese){
		int level=parziale.size();
		
		if(level==NUMERO_GIORNI_TOTALI) {
			soluzione.addAll(parziale);
			costMin = Model.costoSequenza(parziale);
			
		}
		
		
		List<Rilevamento> rilevamentiOggi = dao.getAllRilevamentiGiornoMese(level + 1, mese);
		
		for (Rilevamento r: rilevamentiOggi) {
			parziale.add(r);
			if(sequenzaValida(parziale) && costoSequenza(parziale)<this.costMin) 
				recursive(parziale, mese);

		}
		
		parziale.remove(level);
		
	}
	

	
	
	
	private static boolean sequenzaValida(List<Rilevamento> seq) {
		int countM = 0;
		int countT = 0;
		int countG = 0;
		int countSeq = 0;

		for (int i = 0; i < seq.size(); i++) {
			Rilevamento r = seq.get(i);
			char citta = r.getLocalita().charAt(0);
			

			if (i > 0) {
				char vecchiaCitta = seq.get(i-1).getLocalita().charAt(0);
				if (citta != vecchiaCitta) {
					if (countSeq < NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
						return false;
					} else {
						countSeq = 0;
					}
				}
			}
			countSeq++;

			switch (citta) {
			case 'M':
				countM++;
				break;
			case 'T':
				countT++;
				break;
			case 'G':
				countG++;
				break;
			default:
				return false;
			}

			if (countM > NUMERO_GIORNI_CITTA_MAX || countT > NUMERO_GIORNI_CITTA_MAX || countG > NUMERO_GIORNI_CITTA_MAX) {
				return false;
			}
		}
		return true;
	}
	

	private static int costoSequenza(List<Rilevamento> seq) {
		int costo = 0;
		for (int i = 0; i < seq.size() - 1; i++) {
			costo += seq.get(i).getUmidita();
			if (!seq.get(i).getLocalita().contentEquals(seq.get(i+1).getLocalita())) {
				costo += COST;
			}
		}
		costo += seq.get(seq.size()-1).getUmidita();
		return costo;
	}
	
	
/*	void recursive (....,level) {
		
		//livello 0
		
		parziale+=citta
				costoCitta+=costo
		
		
		if(level=14 && costosol<costosolprecendete)
			soluzione.add(parziale);
		
		
		
		for(percorso tale: parziale)
			parziale+=cittagenerica;
		if(cittagenerica same citta prec)
			
		costoparziale+=costopasso;
		
		else
			cosrto parziale+=100+ costopasso;
			
		if(costoparziale<costoparziale precendete)
			recursive(., level+1)
			
			remove cittagenerica;
		*/
		
	}
	
	
	
	
	
