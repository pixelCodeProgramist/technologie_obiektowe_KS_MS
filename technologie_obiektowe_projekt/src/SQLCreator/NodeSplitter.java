package SQLCreator;

import ERDCreator.resources.XTableView;
import javafx.scene.control.Label;
import models.MoveableNodeModel;
import models.TableModel;

import java.util.ArrayList;
import java.util.List;

public class NodeSplitter {
    private MoveableNodeModel moveableNodeModel;
    private Label label;
    private XTableView xTableView;
    private List<TableModel> rowsOfTable;
    private ArrayList<NodeSql> nodeSqls;


    public NodeSplitter(MoveableNodeModel node, ArrayList<NodeSql> nodeSqls) {
        this.moveableNodeModel = node;
        this.label = this.moveableNodeModel.getLabel();
        this.xTableView = this.moveableNodeModel.getxTableView();
        this.rowsOfTable = (List<TableModel>) this.xTableView.getItems();
        this.nodeSqls = nodeSqls;
    }

    private String getTableName() {
        return label.getText();
    }

    public String buildQuery(){
        StringBuilder query = new StringBuilder();
        int inheritenceNumber = (int) moveableNodeModel.getLineConnectionStringMap().entrySet().
                stream().filter(conn -> conn.getKey().getConnectionType().equals("dziedziczenie")).count();
        if(inheritenceNumber!=moveableNodeModel.getLineConnectionStringMap().size()) {
            NodeSql nodeSql = new NodeSql();
            query.append("CREATE TABLE " + getTableName() + " (\n");
            this.rowsOfTable.forEach(row -> {
                query.append(row.getId() + " " + row.getType());
                if (row.isPrimaryKey()) query.append(" NOT NULL PRIMARY KEY");
                if (!row.isPrimaryKey()) {
                    if (row.isUnique()) query.append(" UNIQUE");
                    if (row.isNotNull()) query.append(" NOT NULL");
                }
                if (!row.equals(rowsOfTable.get(rowsOfTable.size() - 1))) query.append(",\n");
            });
            query.append(");\n\n");
            nodeSql.setHeaderAndBody(query.toString());
            nodeSqls.add(nodeSql);
        }

        return query.toString();
    }


}
