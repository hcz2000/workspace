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
    public void process(){
		try(BocwmLoader boc=new BocwmLoader()){
			System.out.println("----处理中银理财 ----");
			boc.refreshCatalog();
			System.out.println("----中银理财处理完成!----");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		try (CgbwmLoader cgb = new CgbwmLoader()) {
			System.out.println("----处理广银理财 ----");
			cgb.refreshCatalog();
			System.out.println("----广银理财处理完成!----");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try(CmbwmLoader cmb=new CmbwmLoader()){
			System.out.println("----处理招银理财 ----");
			cmb.refreshCatalog();
			System.out.println("----招银理财处理完成!----");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		try(PawmLoader pa=new PawmLoader()){
			System.out.println("----处理平安理财 ----");
			pa.refreshCatalog();
			System.out.println("----平安理财处理完成!----");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

