package siru.fileservice.domain.file;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 이미지 파일 도메인
 * @author siru
 */
@Entity
@Table(name = "file")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ImageFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = true)
    private String fileCropUrl;

    @Column(length = 255, nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 100, nullable = false)
    private String mimeType;

    @Column(length = 10, nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Enumerated(EnumType.STRING)
    private FileStatus status;

}