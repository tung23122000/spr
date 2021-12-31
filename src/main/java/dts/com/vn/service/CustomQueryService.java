package dts.com.vn.service;

import dts.com.vn.config.FileStorageConfig;
import dts.com.vn.entities.AutoExtendPackage;
import dts.com.vn.entities.ConstantDeclaration;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.Constant;
import dts.com.vn.repository.AutoExtendPackageRepository;
import dts.com.vn.repository.ConstantDeclarationRepository;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.FileResponse;
import dts.com.vn.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@PersistenceContext
public class CustomQueryService {

	private final ConstantDeclarationRepository configRepository;

	private final Path fileStorageLocation;

	private final AutoExtendPackageRepository autoExtendPackageRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public CustomQueryService(ConstantDeclarationRepository configRepository,
	                          FileStorageConfig config,
	                          AutoExtendPackageRepository autoExtendPackageRepository) {
		this.configRepository = configRepository;
		this.fileStorageLocation = Paths.get(config.getUploadDir()).toAbsolutePath().normalize();
		this.autoExtendPackageRepository = autoExtendPackageRepository;
	}

	/**
	 * Description - Save file to server
	 *
	 * @param file - as Multipartfile
	 * @return any
	 * @author - giangdh
	 * @created - 9/8/2021
	 */
	public String storeFileToServer(MultipartFile file) throws IOException {
		// Normalize file name
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		try {
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return fileName;
		} catch (IOException ex) {
			throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	/**
	 * Description - Lấy về toàn bộ file trong folder upload
	 *
	 * @param config - Đường dẫn folder
	 * @return any
	 * @author - giangdh
	 * @created - 9/9/2021
	 */
	public List<FileResponse> getAllFiles(FileStorageConfig config) throws IOException {
		List<FileResponse> result = new ArrayList<>();
		List<File> files = Files.list(Paths.get(config.getUploadDir()))
				.map(Path::toFile)
				.collect(Collectors.toList());
		List<ConstantDeclaration> listConfig = configRepository.findAll();
		for (File item : files) {
			FileResponse file = new FileResponse();
			file.setFileName(item.getName());
			SimpleDateFormat df = new SimpleDateFormat(DateTimeUtil.DD_MM_YYYY_HH_mm_ss);
			String date = df.format(new Date(item.lastModified()));
			file.setUploadDate(date);
			for (ConstantDeclaration configSystem : listConfig) {
				if (item.getName().equalsIgnoreCase(configSystem.getConstantKey())) {
					file.setConfigTime(configSystem.getConstantValue());
				}
			}
			result.add(file);
		}
		return result;
	}

	/**
	 * Description - Run SQL from user
	 *
	 * @param sql - as string
	 * @return any
	 * @author - giangdh
	 * @created - 9/9/2021
	 */
	@Transactional
	public ApiResponse excuteQuery(String sql) {
		if (sql.contains("auto_extend_package")) {
			Query query = entityManager.createNativeQuery(sql);
			if (sql.contains("insert")) {
				List<AutoExtendPackage> listExistRecord = autoExtendPackageRepository.findAll();
				if (listExistRecord.size() > 0) {
					boolean isDuplicateRecord = false;
					for (AutoExtendPackage item : listExistRecord) {
						String strInsertDate = DateTimeUtil.formatInstantWithTimezone(item.getInsertDate(), "yyyy-MM-dd HH:mm:ss", "Asia/Ho_Chi_Minh");
						if (sql.contains(item.getIsdn()) && sql.contains(item.getPackageId().toString()) && sql.contains(item.getPackageId().toString()) && sql.contains(strInsertDate)) {
							isDuplicateRecord = true;
							break;
						}
					}
					if (!isDuplicateRecord) {
						query.executeUpdate();
						return new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null, null, Constant.EXCUTE_QUERY_SUCCESS + " Thêm mới dữ liệu thành công");
					} else {
						return new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, null, Constant.EXCUTE_QUERY_FAIL + " " + Constant.DUPLICATE_RECORD);
					}
				} else {
					query.executeUpdate();
					return new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null, null, Constant.EXCUTE_QUERY_SUCCESS + " Thêm mới dữ liệu thành công");
				}
			}
			List<Object> list = query.getResultList();
			if (list.size() > 0) {
				return new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list, null,
						Constant.EXCUTE_QUERY_SUCCESS + " Đã tương tác với " + list.size() + " bản ghi");
			} else {
				return new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null, null, Constant.EXCUTE_QUERY_NO_RECORDS);
			}
		} else {
			return new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, null, Constant.EXCUTE_QUERY_FAIL);
		}
	}

}
