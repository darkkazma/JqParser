

public class Oracle_Item implements Comparable<Oracle_Item>{

  private int ROWNUM;               // Logminer ROWNUM
  private Double SCN;               // Oracle SCN 넘버
  private String OPERATION;         // 쿼리 수행 명령
  private int OPERATION_CODE;       // 쿼리 수행 명령 CODE
  private double START_SCN;         // 쿼리 시작 SCN
  private double COMMIT_SCN;        // 쿼리 커밋 SCN
  private String TID;
  private String XID;               // 트랜잭션 추적 아이디
  private String RS_ID;             // 동일한 트랜잭션의 길이기 길어 다음 row로 넘어가는 경우 동일처리를 위해 
  private int ROLLBACK;             // rollback 여부
  private String SEG_OWNER;         // 데이터 세그먼트 변경 주체
  private String SEG_NAME;          // 데이터 세그먼트 변경 이름
  private String TABLE_NAME;        // 변경 테이블 네임
  private String USER_NAME;         // 변경 유저 네임
  private String SQL_REDO;          // 수행 쿼리
  private Double REDO_VALUE;        // REDO_VALUE
  private String ROW_ID;             // ROWID [데이터의 ROWID]
  private int CSF;                  // 긴문장 처리
  private boolean checking;         // 처리된 SQL구문 확인
  private boolean PLSQL;            // PLSQL 문인지 확인
  private boolean DDL;              // DDL 문인지 확인
  


  public Double getSCN() {
    return SCN;
  }

  public void setSCN(Double sCN) {
    SCN = sCN;
  }

  public int getROWNUM() {
    return ROWNUM;
  }

  public void setROWNUM(int rOWNUM) {
    ROWNUM = rOWNUM;
  }

  public String getOPERATION() {
    return OPERATION;
  }

  public void setOPERATION(String oPERATION) {
    OPERATION = oPERATION;
  }

  public int getOPERATION_CODE() {
    return OPERATION_CODE;
  }

  public void setOPERATION_CODE(int oPERATION_CODE) {
    OPERATION_CODE = oPERATION_CODE;
  }

  public String getSEG_OWNER() {
    return SEG_OWNER;
  }

  public void setSEG_OWNER(String sEG_OWNER) {
    SEG_OWNER = sEG_OWNER;
  }

  public String getSEG_NAME() {
    return SEG_NAME;
  }

  public void setSEG_NAME(String sEG_NAME) {
    SEG_NAME = sEG_NAME;
  }

  public String getTABLE_NAME() {
    return TABLE_NAME;
  }

  public void setTABLE_NAME(String tABLE_NAME) {
    TABLE_NAME = tABLE_NAME;
  }

  public String getSQL_REDO() {
    return SQL_REDO;
  }

  public void setSQL_REDO(String sQL_REDO) {
    SQL_REDO = sQL_REDO;
  }

  public String getUSER_NAME() {
    return USER_NAME;
  }

  public void setUSER_NAME(String uSER_NAME) {
    USER_NAME = uSER_NAME;
  }

  public double getSTART_SCN() {
    return START_SCN;
  }

  public void setSTART_SCN(double sTART_SCN) {
    START_SCN = sTART_SCN;
  }

  public double getCOMMIT_SCN() {
    return COMMIT_SCN;
  }

  public void setCOMMIT_SCN(double cOMMIT_SCN) {
    COMMIT_SCN = cOMMIT_SCN;
  }

  public String getXID() {
    return XID;
  }

  public String getTID() {
    return TID;
  }

  public void setTID(String tID) {
    TID = tID;
  }

  public void setXID(String xID) {
    XID = xID;
  }

  public int getROLLBACK() {
    return ROLLBACK;
  }

  public void setROLLBACK(int rOLLBACK) {
    ROLLBACK = rOLLBACK;
  }
  
  public String getRS_ID() {
    return RS_ID;
  }

  public void setRS_ID(String rS_ID) {
    RS_ID = rS_ID;
  }
  
 

  public int getCSF() {
    return CSF;
  }

  public void setCSF(int cSF) {
    CSF = cSF;
  }

  public Double getREDO_VALUE() {
    return REDO_VALUE;
  }

  public void setREDO_VALUE(Double rEDO_VALUE) {
    REDO_VALUE = rEDO_VALUE;
  }

  public boolean isChecking() {
    return checking;
  }

  public void setChecking(boolean checking) {
    this.checking = checking;
  }

  public boolean isPLSQL() {
    return PLSQL;
  }

  public void setPLSQL(boolean pLSQL) {
    PLSQL = pLSQL;
  }

  public String getROW_ID() {
    return ROW_ID;
  }

  public void setROW_ID(String rOW_ID) {
    ROW_ID = rOW_ID;
  }


  

  public boolean isDDL() {
    return DDL;
  }

  public void setDDL(boolean dDL) {
    DDL = dDL;
  }

  @Override
  public String toString() {
    return "Oracle_Item [ROWNUM=" + ROWNUM + ", SCN=" + SCN + ", OPERATION=" + OPERATION + ", OPERATION_CODE=" + OPERATION_CODE + ", START_SCN=" + START_SCN + ", COMMIT_SCN=" + COMMIT_SCN + ", TID="
        + TID + ", XID=" + XID + ", RS_ID=" + RS_ID + ", ROLLBACK=" + ROLLBACK + ", SEG_OWNER=" + SEG_OWNER + ", SEG_NAME=" + SEG_NAME + ", TABLE_NAME=" + TABLE_NAME + ", USER_NAME=" + USER_NAME
        + ", SQL_REDO=" + SQL_REDO + ", REDO_VALUE=" + REDO_VALUE + ", ROW_ID=" + ROW_ID + ", CSF=" + CSF + ", checking=" + checking + ", PLSQL=" + PLSQL + "]";
  }

  @Override
  public int compareTo(Oracle_Item o) {
    if (this.REDO_VALUE >  o.REDO_VALUE) {  
        return 1;
    } else if (this.REDO_VALUE < o.REDO_VALUE) {
       return -1;
    } else {
       return 0;
    }
  }
  
  
  @Override
  public int hashCode() {
    return (this.TID.hashCode() + this.SQL_REDO.hashCode()); 
  }
  
  @Override
  public boolean equals(Object o){
    
    if( this.getREDO_VALUE() != ((Oracle_Item)o).getREDO_VALUE() 
        && this.getTID().equals(((Oracle_Item)o).getTID()) 
        && this.getOPERATION_CODE() == ((Oracle_Item)o).getOPERATION_CODE() 
        && this.SQL_REDO.equals(((Oracle_Item)o).getSQL_REDO())
    ){
      if( ((Oracle_Item)o).getOPERATION_CODE() == 1 ){
        if( this.getROW_ID().equals( ((Oracle_Item)o).getROW_ID()) ){
          return true;
        }else{ return false; }
      }else{
        return true;
      }
    }else{ return false; }
  }
  
}
