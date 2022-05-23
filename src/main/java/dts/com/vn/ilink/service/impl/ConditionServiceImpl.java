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
     * Description - Lấy danh sách các điều kiện để config trong bảng list_condition
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
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listConditions, null, "Lấy danh sách điều kiện thành công");
        } catch (Exception e) {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, null, "Lấy danh sách điều kiện thất bại");
        }
        return response;
    }

    /**
     * Description - Lấy danh sách điều kiện của 1 chương trình
     *
     * @param programCode - Mã chương trình
     * @param transaction - Luồng
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
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listCondition, null, "Lấy danh sách điều kiện từ ilink thành công");
        } else {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, null, "Không tồn tại bản ghi nào");
        }
        return response;
    }

    /**
     * Description - Tạo mới danh sách điều kiện cho 1 CT vs 1 luồng
     *
     * @param programCode   - Mã chương trình
     * @param transaction   - Luồng
     * @param listCondition - Danh sách điều kiện
     * @return any
     * @author - giangdh
     * @created - 11/16/2021
     */
    @Override
    public ApiResponse createListCondition(String programCode, String transaction, List<Condition> listCondition,
                                           Long packageId) {
        ApiResponse response;
        String key = "\"" + programCode + "#" + transaction + "\"";
        // Tìm xem có bản ghi trùng không
        BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey("LKT_CHECK_CONDITIONS", key);
        if (record != null) {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), record,
                                       null, "Đã tồn tại bản ghi với chương trình và luồng này");
        } else {
            // Build danh sách các điều kiện
            StringBuilder newValue = new StringBuilder();
            // Sắp sếp lại danh sách điều kiện theo thứ tự dựa vào order
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
            // Tạo mới 1 record
            BstLookupTableRow row = new BstLookupTableRow();
            Long rowId = bstLookupTableRowRepository.getMaxRowId("LKT_CHECK_CONDITIONS") + 1;
            row.setTableId(bstLookupTableRepository.findByName("LKT_CHECK_CONDITIONS"));
            row.setRowId(rowId);
            row.setKey(key);
            row.setValue(newValue.toString());
            bstLookupTableRowRepository.saveAndFlush(row);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), row,
                                       null, "Thêm mới danh sách điều kiện thành công");
        }
        return response;
    }

    /**
     * Description - Cập nhật danh sách điều kiện cho 1 CT vs 1 luồng
     *
     * @param programCode   - Mã chương trình
     * @param transaction   - Luồng
     * @param listCondition - Danh sách điều kiện
     * @return any
     * @author - giangdh
     * @created - 11/16/2021
     */
    @Override
    public ApiResponse updateListCondition(String programCode, String transaction, List<Condition> listCondition,
                                           Long packageId) {
        ApiResponse response;
        String key = "\"" + programCode + "#" + transaction + "\"";
        // Tìm bản ghi để update
        BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey("LKT_CHECK_CONDITIONS", key);
        if (record != null) {
            // Build danh sách các điều kiện
            StringBuilder newValue = new StringBuilder();
            // Sắp sếp lại danh sách điều kiện theo thứ tự dựa vào order
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
                                       null, "Cập nhật danh sách điều kiện thành công");
        } else {
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listCondition,
                                       null, "Không tồn tại bản ghi nào phù hợp");
        }
        return response;
    }

    /**
     * Description - Xóa danh sách điều kiện cho 1 CT vs 1 luồng
     *
     * @param programCode - Mã chương trình
     * @param transaction - Luồng
     * @return any
     * @author - giangdh
     * @created - 11/16/2021
     */
    @Override
    public ApiResponse deleteListCondition(String programCode, String transaction) {
        ApiResponse response;
        String key = "\"" + programCode + "#" + transaction + "\"";
        // Tìm bản ghi để update
        BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey("LKT_CHECK_CONDITIONS", key);
        if (record != null) {
            bstLookupTableRowRepository.delete(record);
        }
        response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), record,
                                   null, "Xóa danh sách điều kiện thành công");
        return response;
    }
}
