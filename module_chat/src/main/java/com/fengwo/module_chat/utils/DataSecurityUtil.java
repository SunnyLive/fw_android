package com.fengwo.module_chat.utils;

import com.fengwo.module_websocket.security.SecuritySupport;

public class DataSecurityUtil {

	private static String hexString = "0123456789ABCDEF";

	public static void main(String[] args) {


		
	    
		DataSecurityUtil des = new DataSecurityUtil();
		
		String source = " {\"status\":\"OK\",\"description\":\"\",\"ALL_GARAGES_LIST\":[{\"ids\":\"1112\",\"c_garage_name\":\"特房集团\",\"c_garage_owner\":\"人居乐业物业\",\"c_garage_service_obj_list\":[{\"ids\":\"1078\",\"c_garage_obj_name\":\"办公楼\"}],\"c_garage_type_list\":[{\"ids\":\"1078\",\"c_garage_type_name\":\"露天\"}],\"c_cw_type_list\":[{\"ids\":\"1098\",\"c_cw_type_name\":\"露天\",\"c_cw_type_nums\":\"257\",\"c_already_used\":\"0\",\"c_price_list\":[{\"priceids\":\"965\",\"c_price_time_start\":\"0\",\"c_price_time_end\":\"23\",\"c_cw_price\":\"5\",\"c_cw_hours\":\"4\",\"c_cw_free_time\":\"30\"}]}],\"c_per_org_f\":\"2\",\"c_long_price\":\"\",\"c_long_dis\":\"\",\"c_long_notes\":\"\",\"c_garage_add\":\"文塔路211号\",\"c_lon\":\"118.189203\",\"c_lat\":\"24.494671\",\"c_garage_tel\":\"13806079217\",\"c_garage_time\":\"24小时\",\"c_garage_imges\":\"\",\"c_g_notes\":\"半小时内免费，5元/4小时，每超过1小时加收2元，晚上8点到第二天8点收费10元/次；一天封顶20元\",\"c_img_dir\":\"\",\"c_user_ids\":\"5\",\"c_add_time\":\"2015-01-28 12:43:58\",\"c_garage_is_validate\":\"0\",\"c_garage_is_cooperation\":\"-1\",\"c_cw_hours\":\"4\",\"c_garage_cooperation_type_ids\":\"-1\",\"distance\":\"0.36\",\"cw_type_ids\":\"1098\",\"now_price_ids\":\"965\",\"can_used\":\"257\",\"c_level_list\":[{\"c_level_a\":\"1\",\"c_level_b\":\"0\",\"c_level_c\":\"0\"}],\"c_city_ids\":\"3921\",\"tempg\":\"-1\"},{\"ids\":\"605\",\"c_garage_name\":\"观音山保障性住房\",\"c_garage_owner\":\"全和物业\",\"c_garage_service_obj_list\":[{\"ids\":\"571\",\"c_garage_obj_name\":\"住宅\"}],\"c_garage_type_list\":[{\"ids\":\"571\",\"c_garage_type_name\":\"露天;地下\"}],\"c_cw_type_list\":[{\"ids\":\"591\",\"c_cw_type_name\":\"露天;地下\",\"c_cw_type_nums\":\"103\",\"c_already_used\":\"0\",\"c_price_list\":[{\"priceids\":\"458\",\"c_price_time_start\":\"0\",\"c_price_time_end\":\"23\",\"c_cw_price\":\"0\",\"c_cw_hours\":\"0\",\"c_cw_free_time\":\"0\"}]}],\"c_per_org_f\":\"2\",\"c_long_price\":\"\",\"c_long_dis\":\"\",\"c_long_notes\":\"\",\"c_garage_add\":\"观音山保障性住房\",\"c_lon\":\"118.189604\",\"c_lat\":\"24.49347\",\"c_garage_tel\":\"5928708\",\"c_garage_time\":\"24小时\",\"c_garage_imges\":\"\",\"c_g_notes\":\"无\",\"c_img_dir\":\"\",\"c_user_ids\":\"5\",\"c_add_time\":\"2015-01-28 12:37:18\",\"c_garage_is_validate\":\"0\",\"c_garage_is_cooperation\":\"-1\",\"c_cw_hours\":\"0\",\"c_garage_cooperation_type_ids\":\"-1\",\"distance\":\"0.49\",\"cw_type_ids\":\"591\",\"now_price_ids\":\"458\",\"can_used\":\"103\",\"c_level_list\":[{\"c_level_a\":\"0\",\"c_level_b\":\"0\",\"c_level_c\":\"0\"}],\"c_city_ids\":\"3921\",\"tempg\":\"-1\"},{\"ids\":\"1038\",\"c_garage_name\":\"软件园二期\",\"c_garage_owner\":\"特房物业\",\"c_garage_service_obj_list\":[{\"ids\":\"1004\",\"c_garage_obj_name\":\"办公楼\"}],\"c_garage_type_list\":[{\"ids\":\"1004\",\"c_garage_type_name\":\"露天;地下\"}],\"c_cw_type_list\":[{\"ids\":\"1024\",\"c_cw_type_name\":\"露天;地下\",\"c_cw_type_nums\":\"5220\",\"c_already_used\":\"0\",\"c_price_list\":[{\"priceids\":\"891\",\"c_price_time_start\":\"0\",\"c_price_time_end\":\"23\",\"c_cw_price\":\"5\",\"c_cw_hours\":\"4\",\"c_cw_free_time\":\"30\"}]}],\"c_per_org_f\":\"2\",\"c_long_price\":\"\",\"c_long_dis\":\"\",\"c_long_notes\":\"\",\"c_garage_add\":\"前埔软件园观日路\",\"c_lon\":\"118.187414\",\"c_lat\":\"24.489911\",\"c_garage_tel\":\"5953936\",\"c_garage_time\":\"24小时\",\"c_garage_imges\":\"\",\"c_g_notes\":\"半小时内免费，5元/4小时，每超过1小时加收1元；一天封顶15元\",\"c_img_dir\":\"\",\"c_user_ids\":\"5\",\"c_add_time\":\"2015-01-28 12:42:58\",\"c_garage_is_validate\":\"0\",\"c_garage_is_cooperation\":\"-1\",\"c_cw_hours\":\"4\",\"c_garage_cooperation_type_ids\":\"-1\",\"distance\":\"0.81\",\"cw_type_ids\":\"1024\",\"now_price_ids\":\"891\",\"can_used\":\"5220\",\"c_level_list\":[{\"c_level_a\":\"0\",\"c_level_b\":\"0\",\"c_level_c\":\"0\"}],\"c_city_ids\":\"3921\",\"tempg\":\"-1\"},{\"ids\":\"1868\",\"c_garage_name\":\"明发园B区\",\"c_garage_owner\":\"百事达物业\",\"c_garage_service_obj_list\":[{\"ids\":\"1835\",\"c_garage_obj_name\":\"住宅\"}],\"c_garage_type_list\":[{\"ids\":\"1835\",\"c_garage_type_name\":\"地面\"}],\"c_cw_type_list\":[{\"ids\":\"1855\",\"c_cw_type_name\":\"路面\",\"c_cw_type_nums\":\"59\",\"c_already_used\":\"0\",\"c_price_list\":[{\"priceids\":\"1721\",\"c_price_time_start\":\"0\",\"c_price_time_end\":\"23\",\"c_cw_price\":\"0\",\"c_cw_hours\":\"0\",\"c_cw_free_time\":\"0\"}]}],\"c_per_org_f\":\"2\",\"c_long_price\":\"\",\"c_long_dis\":\"\",\"c_long_notes\":\"\",\"c_garage_add\":\"岭兜西二里7-14号\",\"c_lon\":\"118.183258\",\"c_lat\":\"24.488894\",\"c_garage_tel\":\"5198566\",\"c_garage_time\":\"24小时\",\"c_garage_imges\":\"\",\"c_g_notes\":\"无\",\"c_img_dir\":\"\",\"c_user_ids\":\"5\",\"c_add_time\":\"2015-01-28 13:01:58\",\"c_garage_is_validate\":\"0\",\"c_garage_is_cooperation\":\"-1\",\"c_cw_hours\":\"0\",\"c_garage_cooperation_type_ids\":\"-1\",\"distance\":\"0.99\",\"cw_type_ids\":\"1855\",\"now_price_ids\":\"1721\",\"can_used\":\"59\",\"c_level_list\":[{\"c_level_a\":\"0\",\"c_level_b\":\"0\",\"c_level_c\":\"0\"}],\"c_city_ids\":\"3921\",\"tempg\":\"-1\"},{\"ids\":\"973\",\"c_garage_name\":\"明发园A区\",\"c_garage_owner\":\"佰仕达物业\",\"c_garage_service_obj_list\":[{\"ids\":\"939\",\"c_garage_obj_name\":\"住宅\"}],\"c_garage_type_list\":[{\"ids\":\"939\",\"c_garage_type_name\":\"露天;地下\"}],\"c_cw_type_list\":[{\"ids\":\"959\",\"c_cw_type_name\":\"露天;地下\",\"c_cw_type_nums\":\"36\",\"c_already_used\":\"0\",\"c_price_list\":[{\"priceids\":\"826\",\"c_price_time_start\":\"0\",\"c_price_time_end\":\"23\",\"c_cw_price\":\"0\",\"c_cw_hours\":\"0\",\"c_cw_free_time\":\"0\"}]}],\"c_per_org_f\":\"2\",\"c_long_price\":\"\",\"c_long_dis\":\"\",\"c_long_notes\":\"\",\"c_garage_add\":\"岭兜西二里1-6号\",\"c_lon\":\"118.183477\",\"c_lat\":\"24.488784\",\"c_garage_tel\":\"5022006\",\"c_garage_time\":\"24小时\",\"c_garage_imges\":\"\",\"c_g_notes\":\"无\",\"c_img_dir\":\"\",\"c_user_ids\":\"5\",\"c_add_time\":\"2015-01-28 12:42:06\",\"c_garage_is_validate\":\"0\",\"c_garage_is_cooperation\":\"-1\",\"c_cw_hours\":\"0\",\"c_garage_cooperation_type_ids\":\"-1\",\"distance\":\"0.99\",\"cw_type_ids\":\"959\",\"now_price_ids\":\"826\",\"can_used\":\"36\",\"c_level_list\":[{\"c_level_a\":\"0\",\"c_level_b\":\"0\",\"c_level_c\":\"0\"}],\"c_city_ids\":\"3921\",\"tempg\":\"-1\"}],\"COUNTS\":\"1919\"}";
		String data = "2o85pOnsT3nof7JoO3F9QMu4MiO7wKTgCt20l7Mo9VCBL+EPh8bf7V/dlg2gmAvPeNE/E5V6Vr5q"+
						"1vEfGNxFvJpEFNeFuSoYz5Z8sebE3VdLLfQg/qHaKAUYYtSCrn7bXubdXUDLxiwB8fm1kpoRKnnU"+
						"Ops4SOGXo1y226fv5cEe3mNygDtJYakH9V2VE9wX3hzgEJAPLD75Qm3i1aE29SA/dPkNWGFHXnVN"+
						"lY6kfk68DW/9YT/kFgMSzjUVtT6h341XFGtrVNlgyGBFA9hTfnMfyH7Zk9vyhB71eQrlw6RvMmwA"+
						"OPA9VcQXVWt03V6m7U1g3r/6raQkIAdL5pirqbcZRXQfmKjCtur4XqoH1ZJqscaSwqBGCyUPajTe"+
						"SDNZORwbrq9c+sAiBp3nHlOpQhE64e25H9pwgj+Hl85p4ETdzcbWAlgRfha7Zy3gTQ0QaT66rHRO"+
						"sZY4Mv6LXbXgeybHa3lpaKZvAzhXiMHpIxnmcuogS6m7o3bsrXwaXT8zqEc7ClEmnZ6PlPW+p/ne"+
						"lyQ8WlhiZJ1WJj0J/uX/ACsCflNaxc9OstmmyFE76E7c4bt6XXWXZm9TxqYrtSY0UNBpxqksmYVq"+
						"Xw0cmkseQOq8pOlrekoJx/GL5E3qxafFjHoPulGKCVhfrDVjCxkXoy21u3bUauHIlEg4UEZZnS7K"+
						"aeOPQkAvZWkK9Z5aZaJreBJ/MWJypiGJ7sHpgWfAFe2v85Vqu/5RS3mHs1vOjQKFXpNgs0t97vyY"+
						"5Ske6CqY3KF6Lw5S4VnpQkIf0W5jwNY+7kudjNcT0l6bIexpyj69S1zviqdkMLUYpa1/e6Wktf2R"+
						"enIxL5GCRRhvHYk+xJ66tPb7/kG2m30R27yyO9+ur7pqraW3gJ252QdhzCBZ9Kq8ZIEo3WjxVugA"+
						"SWo5h08nlWuic5h6J12Dowrm2EK7zqya3CQJJbQWsFwe7eoZqo3ysKSAF7YPplpG6Rg67+fSbMyN"+
						"TklB3X7CrHyb5sRu0nLj/M/395eZjjXchmYJQ49ELCUgMjsvtSdCDWbVGOZoOfQ2musYcKbNe6wb"+
						"HNg9gHTzwYAbLZhoT1QdVGtxS0nzUxmQAIurY9M83f4y6G8ybAA48D1VxBdVa3TdXqaXd3qwBGsd"+
						"hWkC4UNpPvPzltS6b4SMQ8lGQCwHLNOibU2O7KTLldGOcp0Wi3BCYmx38/pO2GmGBTweWuX+ytt2"+
						"kNUScYnCu+hP25tQFI/TQ2+bCjcebFJ8IWDJLYlBui6KWPcWfiO8H6KH+iufrt1IItFsn0Nr3iXA"+
						"mMB4Cxgw5AKmVMSTWayKsxRgCIQy5hBMBdEVEH9nvzbw6OH5uveFUtBz4TOVogrgj4Opg0e3/XuT"+
						"izYYS1IAozQv3/HG8NTZUymrQbxJ4kNljTh6L9he64ag07HbT6oKpUj1ToQtlON56u0PTWV7uilR"+
						"84M8Fa4EjHJMRfFst0/bm1AUj9NDMdYjgvMqdJL1UNsxKGRrZJVZpej6xy2wTOXMZrbL5yyjQ76a"+
						"QJqKL5ula4XKfEezmVwtwzI7hEcp3YuOP5i7Y4Z0dMcYYRZU5trrrFLKOARPrNoYfWQF0/RLyTVB"+
						"4APnhdSpRKXlYydWwFK4dynQZRnnMAO8iyF9oMNak6miX3LxZs9XROpqZ1SppDQ/73adM9Kx93JZ"+
						"gjFLeYezW86NAn0OJV+Hnvw43q8KPNlGYLto3dFuFIAJBQiuN0oFN3xRkx6jSq/jFE4=";
		
		long startTime = System.currentTimeMillis(); // 获取开始时间
		System.out.println(des.decrypt(data));
		String esc = des.encrypt(source);
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("加密时间： " + (endTime - startTime) + "ms");
		System.out.println(esc);
		


	}

	public String encrypt(String encryptString) {
		
		String rs = "";
		try {
			rs = SecuritySupport.encrypt(encryptString) ;
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return rs;
	}  
	
	public String decrypt(String decryptString) {
		
		String rs = "";
		
		try {
			rs = SecuritySupport.decrypt(decryptString) ;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rs;
	}  
	

	
}
