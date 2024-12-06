package com.jams.finance;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jams.finance.loader.BocwmLoader;
import com.jams.finance.loader.CgbwmLoader;
import com.jams.finance.loader.CmbwmLoader;
import com.jams.finance.loader.PawmLoader;

@EnableScheduling
@Component
public class Task {
	@Scheduled(cron="0 20 8,13 * * ?")
    private void process(){
		try(BocwmLoader boc=new BocwmLoader()){
			boc.refreshCatalog();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		try (CgbwmLoader cgb = new CgbwmLoader()) {
			cgb.refreshCatalog();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try(CmbwmLoader cmb=new CmbwmLoader()){
			cmb.refreshCatalog();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		try(PawmLoader pa=new PawmLoader()){
			pa.refreshCatalog();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

