package kr.co.luna.club.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ClubMember extends BaseEntity{
    @Id
    private String email;
    private String password;
    private String name;
    private boolean fromSocial;
    //권한을 하나만 가지는 경우
    //private ClubMemberRole rols;

    //권한을 여러개 가질 수 있는 경우
    @Builder.Default
    @ElementCollection(fetch= FetchType.LAZY)
    private Set<ClubMemberRole> roleSet = new HashSet<>();

    //권한을 추가하는 메소드
    public void addMemberRole(ClubMemberRole clubMemberRole){roleSet.add(clubMemberRole);
    }
}
