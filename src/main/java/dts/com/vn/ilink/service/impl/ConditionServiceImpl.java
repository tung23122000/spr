package dts.com.vn.ilink.service.impl;

import dts.com.vn.entities.ListCondition;
import dts.com.vn.entities.MapConditionProgram;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.constants.IlinkTableName;
import dts.com.vn.ilink.dto.ConditionResponse;
import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.entities.Condition;
import dts.com.vn.ilink.repository.BstLookupTableRepository;
import dts.com.vn.ilink.repository.BstLookupTableRowRepository;
import dts.com.vn.ilink.service.ConditionService;
import dts.com.vn.repository.ListConditionRepository;
import dts.com.vn.repository.MapConditionProgramRepository;
import dts.com.vn.repository.ServiceProgramRepository;
import dts.com.vn.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConditionServiceImpl implements ConditionService {

    private final ListConditionRepository listConditionRepository;

    private final BstLookupTableRowRepository bstLookupTableRowRepository;

    private final MapConditionProgramRepository mapConditionProgramRepository;

    private final ServiceProgramRepository serviceProgramRepository;

    private final BstLookupTableRepository bstLookupTableRepository;

    @Autowired
    public ConditionServiceImpl(ListConditionRepository listConditionRepository,
                                BstLookupTableRowRepository bstLookupTableRowRepository,
                                MapConditionProgramRepository mapConditionProgramRepository,
                                ServiceProgramRepository serviceProgramRepository,
                                BstLookupTableRepository bstLookupTableRepository) {
        this.listConditionRepository = listConditionRepository;
        this.bstLookupTableRowRepository = bstLookupTableRowRepository;
        this.mapConditionProgramRepository = mapConditionProgramRepository;
        this.serviceProgramRepository = serviceProgramRepository;
        this.bstLookupTableRepository = bstLookupTableRepository;
    }

    /**
     * Description - L???y danh s??ch c??c ??i???u ki???n ????? config trong b???ng list_condition
     *
     * @return any
     * @author - giangdh
     * @created - 11/12/2021
     */
    @Override
    public ApiResponse findAllCondition() {
        ApiResponse response;
        try {
            List<ListCondition> listConditions = listConditionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listConditions, null, "L???y danh s??ch ??i???u ki???n th??nh c??ng");
        } catch (Exception e) {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, null, "L???y danh s??ch ??i???u ki???n th???t b???i");
        }
        return response;
    }

    /**
     * Description - L???y danh s??ch ??i???u ki???n c???a 1 ch????ng tr??nh
     *
     * @param programCode - M?? ch????ng tr??nh
     * @param transaction - Lu???ng
     * @return ApiResponse
     * @author - giangdh
     * @created - 11/12/2021
     */
    @Override
    public ApiResponse findConditionByProgramCodeAndTransaction(String programCode, String transaction,
                                                                Long packageId) {
        ApiResponse response;
        String key = "\"" + programCode + "#" + transaction + "\"";
        BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey(IlinkTableName.LKT_CHECK_CONDITIONS, key);
        if (record != null) {
            List<ConditionResponse> listCondition = new ArrayList<>();
            if (record.getValue() != null) {
                int order = 1;
                List<String> listConditonFromIlink = new ArrayList<>(Arrays.asList(record.getValue().split(",,")));
                for (int i = 0; i < listConditonFromIlink.size(); i++) {
                    if (!listConditonFromIlink.get(i).equals("\"" + "\"") && listConditonFromIlink.get(i)
                                                                                                  .replaceAll("\"", "")
                                                                                                  .length() > 0) {
                        ConditionResponse condition = new ConditionResponse();
                        String[] value = listConditonFromIlink.get(i).replaceAll("\"", "").split("#");
                        condition.setConditionName(value[1].trim());
                        condition.setIlinkServiceName(value[0].trim());
                        condition.setOrder(String.valueOf(i + 1));
                        order++;
                        condition.setIsSPR(false);
                        listCondition.add(condition);
                    }
                }
                Long id = serviceProgramRepository.findProgramIdByCode(programCode, packageId);
                List<MapConditionProgram> mapConditionProgramList = mapConditionProgramRepository.findByProgramId(id);
                for (int i = 0; i < mapConditionProgramList.size(); i++) {
                    ConditionResponse condition = new ConditionResponse();
                    Optional<ListCondition> conditions =
                            listConditionRepository.findById(mapConditionProgramList.get(i).getConditionId().getId());
                    if (conditions.isPresent()&&mapConditionProgramList.get(i).getTransaction().equalsIgnoreCase(transaction)) {
                        condition.setConditionName(conditions.get().getConditionName());
                        condition.setIlinkServiceName(conditions.get().getIlinkServiceName());
                        condition.setOrder(String.valueOf(order + i));
                        condition.setIsSPR(true);
                        listCondition.add(condition);
                    }
                }
            }
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listCondition, null, "L???y danh s??ch ??i???u ki???n t??? ilink th??nh c??ng");
        } else {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, null, "Kh??ng t???n t???i b???n ghi n??o");
        }
        return response;
    }

    /**
     * Description - T???o m???i danh s??ch ??i???u ki???n cho 1 CT vs 1 lu???ng
     *
     * @param programCode   - M?? ch????ng tr??nh
     * @param transaction   - Lu???ng
     * @param listCondition - Danh s??ch ??i???u ki???n
     * @return any
     * @author - giangdh
     * @created - 11/16/2021
     */
    @Override
    public ApiResponse createListCondition(String programCode, String transaction, List<Condition> listCondition,
                                           Long packageId) {
        ApiResponse response;
        String key = "\"" + programCode + "#" + transaction + "\"";
        // T??m xem c?? b???n ghi tr??ng kh??ng
        BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey("LKT_CHECK_CONDITIONS", key);
        if (record != null) {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), record,
                                       null, "???? t???n t???i b???n ghi v???i ch????ng tr??nh v?? lu???ng n??y");
        } else {
            // Build danh s??ch c??c ??i???u ki???n
            StringBuilder newValue = new StringBuilder();
            // S???p s???p l???i danh s??ch ??i???u ki???n theo th??? t??? d???a v??o order
            listCondition.sort(Comparator.comparing(Condition::getOrder));
            mapConditionProgramRepository.deleteByProgramId(serviceProgramRepository.findProgramIdByCode(programCode,
                                                                                                         packageId),
                                                            transaction);
            for (int i = 0; i < listCondition.size(); i++) {
                String conditionName = "\"" + listCondition.get(i).getConditionName() + "\"";
                if (!listCondition.get(i).getIsSPR()) {
                    if (listCondition.get(i).equals(listCondition.get(listCondition.size() - 1))) {
                        if (!listCondition.get(i).getIsSPR()) {
                            newValue.append(conditionName);
                        }
                    } else {
                        if (!listCondition.get(i).getIsSPR()) {
                            newValue.append(conditionName).append(",,");
                        }
                    }
                } else {
                    MapConditionProgram mapConditionProgram = new MapConditionProgram();
                    mapConditionProgram.setId(null);
                    mapConditionProgram.setConditionValue(null);
                    Long id = serviceProgramRepository.findProgramIdByCode(programCode, packageId);
                    mapConditionProgram.setProgramId(id);
                    String[] nameOfCondition = listCondition.get(i).getConditionName().split("#");
                    mapConditionProgram.setConditionId(listConditionRepository.findConditionIdByName(nameOfCondition[1]));
                    mapConditionProgram.setIsSpr(true);
                    mapConditionProgram.setTransaction(transaction);
                    mapConditionProgramRepository.save(mapConditionProgram);
                }
            }
            // T???o m???i 1 record
            BstLookupTableRow row = new BstLookupTableRow();
            Long rowId = bstLookupTableRowRepository.getMaxRowId("LKT_CHECK_CONDITIONS") + 1;
            row.setTableId(bstLookupTableRepository.findByName("LKT_CHECK_CONDITIONS"));
            row.setRowId(rowId);
            row.setKey(key);
            row.setValue(newValue.toString());
            bstLookupTableRowRepository.saveAndFlush(row);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), row,
                                       null, "Th??m m???i danh s??ch ??i???u ki???n th??nh c??ng");
        }
        return response;
    }

    /**
     * Description - C???p nh???t danh s??ch ??i???u ki???n cho 1 CT vs 1 lu???ng
     *
     * @param programCode   - M?? ch????ng tr??nh
     * @param transaction   - Lu???ng
     * @param listCondition - Danh s??ch ??i???u ki???n
     * @return any
     * @author - giangdh
     * @created - 11/16/2021
     */
    @Override
    public ApiResponse updateListCondition(String programCode, String transaction, List<Condition> listCondition,
                                           Long packageId) {
        ApiResponse response;
        String key = "\"" + programCode + "#" + transaction + "\"";
        // T??m b???n ghi ????? update
        BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey("LKT_CHECK_CONDITIONS", key);
        if (record != null) {
            // Build danh s??ch c??c ??i???u ki???n
            StringBuilder newValue = new StringBuilder();
            // S???p s???p l???i danh s??ch ??i???u ki???n theo th??? t??? d???a v??o order
            listCondition.sort(Comparator.comparing(Condition::getOrder));
            Long id = serviceProgramRepository.findProgramIdByCode(programCode, packageId);
            mapConditionProgramRepository.deleteByProgramId(id, transaction);
            for (int i = 0; i < listCondition.size(); i++) {
                String conditionName = "\"" + listCondition.get(i).getConditionName() + "\"";
                if (!listCondition.get(i).getIsSPR()) {
                    if (listCondition.get(i).equals(listCondition.get(listCondition.size() - 1))) {
                        if (!listCondition.get(i).getIsSPR()) {
                            newValue.append(conditionName);
                        }
                    } else {
                        if (!listCondition.get(i).getIsSPR()) {
                            newValue.append(conditionName).append(",,");
                        }
                    }
                } else {
                    String[] nameOfCondition = listCondition.get(i).getConditionName().split("#");
                    MapConditionProgram mapConditionProgram = new MapConditionProgram();
                    mapConditionProgram.setId(null);
                    mapConditionProgram.setConditionValue(null);
                    mapConditionProgram.setProgramId(serviceProgramRepository.findProgramIdByCode(programCode, packageId));
                    mapConditionProgram.setConditionId(listConditionRepository.findConditionIdByName(nameOfCondition[1]));
                    mapConditionProgram.setIsSpr(listCondition.get(i).getIsSPR());
                    mapConditionProgram.setTransaction(transaction);
                    mapConditionProgramRepository.save(mapConditionProgram);
                }
            }
            record.setValue(newValue.toString());
            bstLookupTableRowRepository.saveAndFlush(record);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), record,
                                       null, "C???p nh???t danh s??ch ??i???u ki???n th??nh c??ng");
        } else {
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listCondition,
                                       null, "Kh??ng t???n t???i b???n ghi n??o ph?? h???p");
        }
        return response;
    }

    /**
     * Description - X??a danh s??ch ??i???u ki???n cho 1 CT vs 1 lu???ng
     *
     * @param programCode - M?? ch????ng tr??nh
     * @param transaction - Lu???ng
     * @return any
     * @author - giangdh
     * @created - 11/16/2021
     */
    @Override
    public ApiResponse deleteListCondition(String programCode, String transaction) {
        ApiResponse response;
        String key = "\"" + programCode + "#" + transaction + "\"";
        // T??m b???n ghi ????? update
        BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey("LKT_CHECK_CONDITIONS", key);
        if (record != null) {
            bstLookupTableRowRepository.delete(record);
        }
        response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), record,
                                   null, "X??a danh s??ch ??i???u ki???n th??nh c??ng");
        return response;
    }
}
