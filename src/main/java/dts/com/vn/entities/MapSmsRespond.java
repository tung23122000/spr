package dts.com.vn.entities;

import dts.com.vn.request.MapSmsRespondRequest;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "map_sms_respond", schema = "public")
public class MapSmsRespond {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_sms_respond_id")
    private Long mapSmsRespondId;

    @Column(name = "sms_respond")
    private String smsRespond;

    @Column(name = "map_sms_respond")
    private String mapSmsRespond;

    @Column(name = "description")
    private String description;

    @Column(name = "shortcode")
    private String shortcode;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private ServiceProgram serviceProgram;

    public MapSmsRespond() {

    }

    public MapSmsRespond(MapSmsRespondRequest mapSmsRespondRequest) {
        this.mapSmsRespondId = mapSmsRespondRequest.getMapSmsRespondId();
        this.smsRespond = mapSmsRespondRequest.getSmsRespond();
        this.mapSmsRespond = mapSmsRespondRequest.getMapSmsRespond();
        this.description = mapSmsRespondRequest.getDescription();
        this.shortcode = mapSmsRespondRequest.getShortcode();
    }
}
