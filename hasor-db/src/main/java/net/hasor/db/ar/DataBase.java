/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.db.ar;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import net.hasor.db.jdbc.ConnectionCallback;
import net.hasor.db.jdbc.JdbcOperations;
import net.hasor.db.jdbc.core.JdbcTemplate;
import org.more.UndefinedException;
import org.more.convert.ConverterUtils;
/**
 * 用来表示数据库s
 * @version : 2014年10月27日
 * @author 赵永春(zyc@hasor.net)
 */
public final class DataBase {
    /**根据表名创建一个{@link Entity}。*/
    public Entity openEntity(String catalog, String tableName) throws SQLException {
        return new Entity(this.openSechma(catalog, tableName));
    }
    /**根据表名创建一个{@link Entity}。*/
    public Entity openEntity(String tableName) throws SQLException {
        return new Entity(this.openSechma(null, tableName));
    }
    /**根据表名创建一个{@link Sechma}。*/
    public Sechma openSechma(String tableName) throws SQLException {
        return this.openSechma(null, tableName);
    }
    /**根据表名创建一个{@link Sechma}。*/
    public Sechma openSechma(final String catalog, final String tableName) throws SQLException {
        final Sechma sechma = new Sechma(this, catalog, tableName);
        //1.获取表属性。
        this.getJdbc().execute(new ConnectionCallback<Boolean>() {
            public Boolean doInConnection(final Connection con) throws SQLException {
                DatabaseMetaData metaData = con.getMetaData();
                ResultSet resultSet = null;
                //
                //1.验证表
                resultSet = metaData.getTables(sechma.getCatalog(), null, sechma.getName().toUpperCase(), new String[] { "TABLE" });
                if (resultSet.next() == false) {
                    throw new UndefinedException("table " + tableName + " is Undefined.");
                }
                sechma.setSechmaType(resultSet.getString("TABLE_TYPE"));
                sechma.setComment(resultSet.getString("REMARKS"));
                //
                //2.检索列
                resultSet = metaData.getColumns(sechma.getCatalog(), null, sechma.getName(), null);
                while (resultSet.next()) {
                    String colName = resultSet.getString("COLUMN_NAME");//列名称。
                    int colSQLType = resultSet.getInt("DATA_TYPE");//来自 java.sql.Types 的 SQL 类型。
                    //
                    Column col = new Column(colName, colSQLType);
                    col.setMaxSize(resultSet.getInt("COLUMN_SIZE"));//列的大小。
                    int allowEmpty = resultSet.getInt("NULLABLE");//是否允许使用 NULL。 
                    col.setEmpty(allowEmpty == DatabaseMetaData.columnNullable);//明确允许使用null
                    col.setComment(resultSet.getString("REMARKS")); //描述
                    String colDefaultValue = resultSet.getString("COLUMN_DEF");//该列的默认值，当值在单引号内时应被解释为一个字符串（可为 null） 
                    //                    col.setDefaultValue(colDefaultValue);
                    boolean colIdentify = (Boolean) ConverterUtils.convert(Boolean.TYPE, resultSet.getString("IS_AUTOINCREMENT"));//指示此列是否自动增加（Yes，No）
                    col.setIdentify(colIdentify);
                    //
                    sechma.addColumn(col);
                }
                //
                //3.确定主键
                //
                //4.确定唯一约束
                //
                //5.通过权限判断决定列可见
                return null;
            }
        });
        //
        return sechma;
    }
    //
    //
    //
    private DataSource dataSource = null;
    /**获取JDBC接口*/
    public JdbcOperations getJdbc() {
        return new JdbcTemplate(this.dataSource);
    }
    public DataBase(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    //
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }
    public SQLBuilder getSQLBuilder() {
        // TODO Auto-generated method stub
        return null;
    }
    public Identify getIdentify(Sechma sechma) {
        // TODO Auto-generated method stub
        return null;
    }
}