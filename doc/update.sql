update WORK_CERT_INFO c  SET c.CREATE_DATE =  c.SIGN_DATE where c.RENEWAL_PREV_ID is null;

update WORK_CERT_INFO c  SET c.CREATE_DATE =  (select d.CREATE_DATE from   WORK_CERT_INFO d 
where  d.ID =c.RENEWAL_PREV_ID ) where  exists (select 1 
from WORK_CERT_INFO d 
where  d.ID =c.RENEWAL_PREV_ID  
) 



SELECT
	/*COUNT (DISTINCT(D ."ID")),sum(p.MONEY)*/
  d."ID",p.MONEY ,p.BANK_MONEY,p.OPEN_ACCOUNT_MONEY,p.POS_MONEY 
FROM
	WORK_DEAL_INFO D,
	WORK_CERT_INFO c,
	WORK_PAY_INFO P
WHERE
	D .CERT_ID = c."ID"
AND D .PAY_ID = P ."ID"
and d.app_id= 621351 
and d.PRODUCT_ID = 621453
and d.AGENT_ID = 621501
and (d.DEAL_INFO_TYPE =1 or d.DEAL_INFO_TYPE1 =1)
AND D ."YEAR" = 1
AND (
	P .METHOD_MONEY = 1
	OR P .METHOD_POS = 1
)
AND c.CREATE_DATE > "TO_DATE" ('20100501', 'yyyymmdd')
AND c.CREATE_DATE < "TO_DATE" ('20150701', 'yyyymmdd')