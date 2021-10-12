package siru.fileservice.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileUrl;

    private Long userId;

    private String mimeType;

    private String extension;

    private Long size;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Enumerated(EnumType.STRING)
    private FileType type;

    @Enumerated(EnumType.STRING)
    private FileStatus status;
}
