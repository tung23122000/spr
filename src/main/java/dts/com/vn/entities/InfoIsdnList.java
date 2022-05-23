package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "info_isdn_list", schema = "public")
public class InfoIsdnList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name_isdn_list")
    private String nameIsdnList;

    @Column(name = "name_target_folder")
    private String nameTargetFolder;

    @Column(name = "name_target_system")
    private String nameTargetSystem;

    @Column(name = "user_name")
    private String user;

    @Column(name = "pass_word")
    private String password;

    @Column(name = "isdn_list_id")
    private Long isdnListId;

}
