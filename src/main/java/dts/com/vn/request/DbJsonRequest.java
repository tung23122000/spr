package dts.com.vn.request;

import dts.com.vn.entities.FoNameValue;
import dts.com.vn.entities.KeyAndNameDbJson;
import dts.com.vn.entities.Value;
import lombok.Data;

import java.util.List;

@Data
public class DbJsonRequest {

    private List<KeyAndNameDbJson> listServicePackage;

    private List<Value> serviceProgramCode;

    private List<Value> listCatalogFlow;

    private List<Value> listConditionConfig;

    private List<KeyAndNameDbJson> listServicePcrf;

    private List<FoNameValue> listFoName;

}
