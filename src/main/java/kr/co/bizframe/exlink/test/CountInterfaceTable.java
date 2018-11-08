package kr.co.bizframe.exlink.test;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf.FileParserMapConfInfo;
import kr.co.bizframe.exlink.parser.file.AbstractFileGenerator;
import kr.co.bizframe.exlink.parser.file.plugin.ByteBlockFileParser;
import kr.co.bizframe.exlink.parser.file.plugin.ByteIndicatorFileParser;
import kr.co.bizframe.exlink.parser.file.plugin.JsonFileGenerator;
import kr.co.bizframe.exlink.sql.SqlExecutor;
import kr.co.bizframe.exlink.sql.SqlFetcher;
import kr.co.bizframe.exlink.sql.scirpt.SqlParser;
import kr.co.bizframe.exlink.type.AppendColumnType;
import kr.co.bizframe.exlink.type.GeneratorType;
import kr.co.bizframe.exlink.type.InterfaceType;

public class CountInterfaceTable {

	public static Logger logger = LoggerFactory.getLogger(CountInterfaceTable.class);


	public static final String TABLELIST199[] = {
		"ITBA_ICLANE_MST",  
		"ITBA_LNKFRE_MST",  
		"ITBA_WORKER_DTL",  
		"ITMG_ICTBLS_SET",  
		"ITBU_COLHIP_BAS",  
		"ITBU_COLHIP_AMT",  
		"ITBU_COLHIP_VCD",  
		"ITBU_BYWORK_SUM",  
		"ITBU_BYWORK_SUM_SPE",  
		"ITBU_BYWORK_SUM_AXL",  
		"ITBU_BYWORK_SUM_PAY",  
		"ITTR_INOUTS_TRA",  
		"ITBU_PTICIO_SUM",  
		"ITMG_MCNFIX_DTL",  
		"ITHI_OUTWOK_SUM",  
		"ITHI_OUTWOK_CAR",  
		"ITHI_OUTWOK_SEN",  
		"ITHI_OUTWOK_VIO",  
		"ITMG_BYLANE_AXL_STR",  
		"ITMG_BYLANE_AXL_SUM",  
		"ITBU_CLLANE_SUM",  
		"ITMG_WEATHE_INF",  
		"ITBU_TICDBL_DTL",  
		"ITBU_TICGIV_CNT",  
		"ITBU_TICKIO_DTL",  
		"ITBU_PSOFRE_SUM",  
		"ITBU_PSODSC_SUM",  
		"ITBU_OUTWOK_BAS",  
		"ITBU_OUTWOK_VIO",  
		"ITBU_OUTWOK_PAY_AMT",  
		"ITBU_OUTWOK_PAY_BAS",  
		"ITBU_OUTWOK_CNT",  
		"ITBU_OUTWOK_DCI_AMT",  
		"ITBU_OUTWOK_DCI_BAS",  
		"ITBU_OUTWOK_VIO_HIP",  
		"ITBU_OUTWOK_TCS_BAS",  
		"ITBU_OUTWOK_BAS_AMT",  
		"ITBU_OUTWOK_SPE_PRO",  
		"ITBU_OUTWOK_MOD",  
		"ITBU_OUTECD_ERR_MOD",  
		"ITBU_ETCCOL_DTL",  
		"ITBU_ETCCOL_AMT",  
		"ITBU_PTICWR_DTL",  
		"ITBU_PTICRT_DTL",  
		"ITTR_IOLANE_TRA",  
		"ITBU_INSPEC_DTL",  
		"ITHC_TRANEC_PRO_DTL",  
		"ITBU_LNKCOL_DTL",  
		"ITBU_LNKCOL_AMT",  
		"ITPA_DCINCR_DTL",  
		"ITPA_PRITRA_DTL",  
		"ITBU_DCINCR_SUM",  
		"ITTR_ICBETW_TRA_PRI_IN", 
		"ITBU_PCDFRE_SUM",  
		"ITBU_PTIGIV_CNT",  
		"ITBU_DAYWOK_PRO",  
		"ITBU_DAYWOK_CAR",  
		"IEDE_PAYDTL_NOR",  
		"IEES_ESCCOL_AMT",  
		"IEES_ESCCOL_DTL",  
		"ITHI_ESLANE_COL",  
		"ITBU_OUTWOK_MIX",  
		"ITES_ESLANE_COL",  
		"ITBU_PRITRA_DTL",  
		"ITES_ESLANE_PRO",  
		"ITHI_COLTRK_CNT",  
		"ITHI_COLTRK_AMT",  
		"ITHI_DCTRCK_CNT",  
		"ITHI_DCTRCK_AMT",  
		"ITES_ESCCAR_PRO",  
		"ITHI_OUTTRK_DTL",  
		"ITES_ESCCAR_DTL",  
		"ITES_ESCCAR_COL",  
		"ITHI_OUTTRK_AMT",  
		"ITHI_INTRCK_DTL",  
		"ITHI_OUTPRO_DTL_DUP",  
		"ITHI_OUTPRO_AMT_DUP",  
		"ITMG_OUTSPC_MOD",  
		"IPDE_ICINSP_DTL",  
		"ITBU_COLPAY_AMT",  
		"ITBU_COLPAY_BAS",  
		"ITBU_COLDCI_AMT",  
		"ITBU_COLDCI_BAS",  
		"ITBU_COLVIO_BAS",  
		"ITBU_COLADM_BAS",  
		"ITBU_COLTCS_BAS",  
		"ITBU_COLTCS_SPE",  
		"ITBU_COLTCS_MIX",  
		"ITTR_ICBETW_TRA",  
		"ITTR_ICBDRI_TME",  
		"ITBU_INCOME_LNK_CNT",  
		"ITBU_INCOME_LNK_AMT",  
		"ITBU_DCINCR_DTL",  
		"ITBU_DCINCR_PAT",  
		"ITBU_DCINCR_AMT",  
		"ITBU_OFFICE_DTL",  
		"ITBU_OFFICE_PAT",  
		"ITBU_OFFICE_AMT",  
		"ITBU_OFFICE_MOD",  
		"ITBU_OFFICE_MOD_PAT",  
		"ITBU_OFFICE_MOD_AMT",  
		"ITBU_OUTSPE_DTL",  
		"ITBU_HIPASS_DCI_SUM_CNT",  
		"ITBU_HIPASS_DCI_SUM_AMT",  
		"ITBU_PSOFRE_DTL",  
		"ITBU_PSOFRE_PAT",  
		"ITBU_PSOFRE_AMT",  
		"ITBU_PSODSC_DTL",  
		"ITBU_PSODSC_PAT",  
		"ITBU_PSODSC_AMT",  
		"ITBU_PCDFRE_DTL",  
		"ITBU_PCDFRE_PAT",  
		"ITBU_PCDFRE_AMT",  
		"ITCR_ILLCAR_DTL",  
		"ITBU_COLETC_AMT",  
		"ITBU_COLETC_BAS",  
		"ITBU_COLSUM_BAS",  
		"ITBU_COLSUM_AMT",  
		"ITBU_COLSUM_ADM",  
		"ITES_ILLEGA_DTL",  
		"ITES_ILLEGA_PAT",  
		"ITES_ILLEGA_COL",  
		"ITBU_CASPAY_DTL",  
		"ITBU_CASPAY_PAT",  
		"ITBU_CASPAY_AMT",  
		"ITHI_DCPROC_DTL",  
		"ITHI_DCPROC_PAT",  
		"ITHI_DCPROC_AMT",  
		"IAVI_VIAWOK_SUM",  
		"IAVI_VIACOL_SUM",  
		"ITHI_DCFCEV_DTL",  
		"ITHI_DCFCEV_PAT",  
		"ITHI_DCFCEV_AMT",  
		"ITBU_DCFCEV_SUM",  
		"ITHI_AEBSDC_DTL",  
		"ITHI_AEBSDC_PAT",  
		"ITHI_AEBSDC_AMT",  
		"ITBU_AEBSDC_SUM",  
	};

	public static final String TABLELIST401[] = {
		"IEDE_PAYDTL_NOR",  
	};
	
	public static final String TABLELIST410[] = {
		"ICDE_PAYDTL_NOR#",  
	};
	
	public static final String TABLELIST402[] = {
		"IPDE_PAYDTL_NOR",  
	};
	

}
