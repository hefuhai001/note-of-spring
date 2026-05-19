package asia.hfh.code.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.*;

/**
 * PostgreSQL json/jsonb  ↔  com.fasterxml.jackson.databind.JsonNode
 */
@MappedTypes(JsonNode.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class JsonNodeTypeHandler extends BaseTypeHandler<JsonNode> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /* 把 JsonNode 写进 PG */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    JsonNode parameter, JdbcType jdbcType) throws SQLException {
        PGobject pg = new PGobject();
        pg.setType("json");          // 如果是 jsonb 就写 "jsonb"
        pg.setValue(parameter.toString());
        ps.setObject(i, pg);
    }

    /* 三个 getNullableResult 重载 */
    @Override
    public JsonNode getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toJsonNode(rs.getString(columnName));
    }

    @Override
    public JsonNode getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toJsonNode(rs.getString(columnIndex));
    }

    @Override
    public JsonNode getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toJsonNode(cs.getString(columnIndex));
    }

    /* 工具方法 */
    private JsonNode toJsonNode(String json) throws SQLException {
        if (json == null) return null;
        try {
            return MAPPER.readTree(json);
        } catch (Exception e) {
            throw new SQLException("Failed to parse JSON", e);
        }
    }
}