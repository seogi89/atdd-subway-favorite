package atdd.member.dao;

import atdd.member.domain.Member;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("MEMBER")
            .usingGeneratedKeyColumns("ID");
    }

    public Member save(Member member) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("EMAIL", member.getEmail());
        parameters.put("NAME", member.getName());
        parameters.put("PASSWORD", member.getPassword());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findById(id);
    }

    public Member findById(Long id) {
        Map<String, Object> result = jdbcTemplate.queryForMap(
            "select id , email, name from member where id = ?",
            new Object[]{id}
        );

        return new Member((long) result.get("ID"), (String) result.get("EMAIL") , (String) result.get("NAME"));
    }


}