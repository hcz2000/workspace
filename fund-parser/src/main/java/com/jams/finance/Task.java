package com.jams.finance;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jams.finance.loader.BocwmLoader;
import com.jams.finance.loader.CgbwmLoader;
import com.jams.finance.loader.CmbwmLoader;
import com.jams.finance.loader.PawmLoader;

@Component
public class Task {
	@Scheduled(cron="0 0 8，14 * * ?")
    private void process(){
		try(BocwmLoader boc=new BocwmLoader()){
			boc.refreshCatalog();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		try (CgbwmLoader boc = new CgbwmLoader()) {
			boc.refreshCatalog();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try(CmbwmLoader boc=new CmbwmLoader()){
			boc.refreshCatalog();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		try(PawmLoader boc=new PawmLoader()){
			boc.refreshCatalog();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

