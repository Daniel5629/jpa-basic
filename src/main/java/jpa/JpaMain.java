package jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    private static final Logger log = LoggerFactory.getLogger(JpaMain.class);

    public static void main(String[] args) {

        // persistence unit name: persistence.xml 의 persistence-unit name
        // "EntityManagerFactory" 는 어플리케이션 로딩시점에 하나만 만들어야함(Thread 공유 하면 안됌)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-basic");

        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행 해야함.
        // create transaction
        EntityTransaction tx = em.getTransaction();

        // begin transaction
        tx.begin();

        try {
            // Member 생성(비영속)
            Member member = new Member();
            member.setId(1L);
            member.setName("daniel");

            // 영속 - DB 저장된 상태 아님.
            em.persist(member);

            // Member 조회
            Member findMember = em.find(Member.class, 1L);
            log.info("=== Member 생성 후 조회 ===");
            log.info("### Member id => {} ###", findMember.getId());
            log.info("### Member name => {} ###", findMember.getName());
            
            // Member 수정
            findMember.setName("Dev.daniel");
            log.info("=== Member 수정 후 조회 ===");
            log.info("### Member id => {} ###", findMember.getId());
            log.info("### Member name => {} ###", findMember.getName());

            // JPQL: SQL을 추상화한 객체 지향 쿼리 언어
            // JPQL은 엔티티 객체를 대상으로 쿼리를 작성한다.
            List<Member> memberList = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            for (Member m : memberList) {
                log.info("=== Using JPQL ===");
                log.info("### Member name => {} ###", m.getName());
            }

            // Member 삭제
            em.remove(findMember);

            // commit transaction
            // DB에 쿼리가 반영
            tx.commit();
            
        } catch (Exception e) {
            // 에러시 rollback
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
