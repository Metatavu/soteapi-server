package fi.metatavu.soteapi.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

import org.wildfly.clustering.group.Group;
import org.wildfly.clustering.group.Node;

/**
 * Cluster controller bean
 * 
 * @author Antti Leppä
 */
@ApplicationScoped
@Singleton
public class ClusterController {

  @Resource(lookup = "java:jboss/clustering/group/web")  
  private Group channelGroup;  

  /**
   * Returns local node's name
   * 
   * @return local node's name
   */
  @Lock(LockType.READ)
  public String getLocalNodeName() {
    Node localNode = channelGroup.getLocalNode();
    if (localNode != null) {
      return localNode.getName();
    }
    
    return "UNKNOWN";
  }
  
  /**
   * Returns list of cluster node names
   * 
   * @return list of cluster node names
   */
  @Lock(LockType.READ)
  public List<String> getNodeNames() {
    List<Node> nodes = channelGroup.getNodes();
    
    List<String> result = new ArrayList<>(nodes.size());
    for (Node node : nodes) {
      result.add(node.getName());
    }
    
    Collections.sort(result);
    
    return result;
  }
  
}
