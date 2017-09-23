package com.coopsystem.web.rest.util;

import com.coopsystem.domain.DocumentationCatalogue;
import com.coopsystem.domain.ProjectDocumentation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz Niedośpiał on 30.05.2017.
 */
public class DocumentationNode {

    private final Long idOfDocElement;
    private final String label;
    private final String data;
    private final DocumentationCatalogue parent;
    private final boolean isDocument;

    private List<DocumentationNode> children = new ArrayList<>();

    public DocumentationNode(Long idOfDocElement, String label, String data, DocumentationCatalogue parent, boolean isDocument) {
        this.idOfDocElement = idOfDocElement;
        this.label = label;
        this.data = data;
        this.parent = parent;
        this.isDocument = isDocument;
    }

    public Long getIdOfDocElement() {
        return idOfDocElement;
    }

    public String getLabel() {
        return label;
    }

    public String getData() {
        return data;
    }

    public List<DocumentationNode> getChildren() {
        return children;
    }

    public DocumentationCatalogue getParent() {
        return parent;
    }

    public boolean isDocument() {
        return isDocument;
    }

    public void setChildren(List<DocumentationNode> children) {
        this.children = children;
    }

    public void addChild(DocumentationNode child){
        children.add(child);
    }

    public DocumentationNode getChild(int index){
        return children.get(index);
    }

    public static DocumentationNode createDocNodeTree(List<DocumentationCatalogue> catalogues, List<ProjectDocumentation> documentations){
        List<DocumentationNode> docNodeTree = new ArrayList<>();
        DocumentationNode root = null;
        catalogues.forEach(catalogue ->  docNodeTree.add(new DocumentationNode(catalogue.getId(), catalogue.getLabel(), catalogue.getData(), catalogue.getParent(), false)));

        List<DocumentationNode> docNodeTreeCopy = new ArrayList<>(docNodeTree);

        for(DocumentationNode docNode: docNodeTree){
            if(docNode.getParent() != null){
                Long idOfParent = docNode.getParent().getId();
                for(DocumentationNode docNodeAgain: docNodeTreeCopy){
                    if(docNodeAgain.idOfDocElement.equals(idOfParent)){
                        docNodeAgain.addChild(docNode);
                    }
                }
            }
        }

        if(docNodeTreeCopy.isEmpty()){
            return null;
        }

        root = docNodeTreeCopy.get(0);

        docNodeTree.clear();
        docNodeTreeCopy.clear(); // for GC
        docNodeTreeCopy = null; // for GC

        if(root != null){
            List<DocumentationNode> startChildren = new ArrayList<>();
            startChildren.add(root);

            for(ProjectDocumentation doc: documentations){
                Long idOfCatalogue = doc.getUnderCatalogue().getId();
                recursiveDocumentationPlacement(startChildren, doc, idOfCatalogue);
            }
        }

        return root;
    }

    private static void recursiveDocumentationPlacement(List<DocumentationNode> children, ProjectDocumentation doc, Long idOfCatalogueThatItsUnder){
        boolean isAdded = false;
        if(! children.isEmpty()){
            for(DocumentationNode docNode: children){
                if(docNode.idOfDocElement.equals(idOfCatalogueThatItsUnder)){
                    docNode.addChild(new DocumentationNode(doc.getId(), doc.getLabel(), doc.getData(), null, true));
                    isAdded = true;
                }
            }
            if(! isAdded){
                for(DocumentationNode docNode: children){
                    recursiveDocumentationPlacement(docNode.children, doc, idOfCatalogueThatItsUnder);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static JSONObject buildJSON(DocumentationNode root){
        if(root == null){
            return new JSONObject();
        }
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        JSONObject rootJSON = new JSONObject();
        rootJSON.put("label", root.getLabel());
        rootJSON.put("data", root.getData());
        rootJSON.put("expandedIcon", "fa-folder-open");
        rootJSON.put("collapsedIcon", "fa-folder");

        JSONArray rootJSONArray = new JSONArray();
        JSONArray rootChildren = buildChildrenJSONRecursively(rootJSONArray, root, root.children);

        rootJSON.put("children", rootChildren);
        jsonArray.add(rootJSON);
        json.put("data", jsonArray);
        return json;
    }

    @SuppressWarnings("unchecked")
    private static JSONArray buildChildrenJSONRecursively(JSONArray startJSONArray, DocumentationNode startDocNode, List<DocumentationNode> children){
        if(! children.isEmpty() && (! startDocNode.isDocument)){
            for(DocumentationNode docuNode: children){
                if(! docuNode.isDocument){
                    JSONObject subJsonObj = new JSONObject();
                    subJsonObj.put("label", docuNode.getLabel());
                    subJsonObj.put("data", docuNode.getData());
                    subJsonObj.put("expandedIcon", "fa-folder-open");
                    subJsonObj.put("collapsedIcon", "fa-folder");
                    JSONArray newJSONArray = new JSONArray();
                    JSONArray subChildren = buildChildrenJSONRecursively(newJSONArray, docuNode, docuNode.children);
                    subJsonObj.put("children", subChildren);
                    startJSONArray.add(subJsonObj);
                } else {
                    JSONObject subJsonObj = new JSONObject();
                    subJsonObj.put("label", docuNode.getLabel());
                    subJsonObj.put("icon", "fa-file-word-o");
                    subJsonObj.put("data", docuNode.getData());
                    startJSONArray.add(subJsonObj);
                }
            }
            return startJSONArray;
        } else {
            return startJSONArray;
        }
    }
}
