import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { Button, Result, Tree } from 'antd';
import ProTable from '@ant-design/pro-table';
import React, { useState } from 'react';
import { history } from 'umi';
import cytoscape from "cytoscape";
import klay from "cytoscape-klay";
import { getCallerGraph } from '@/services/ant-design-pro/callgraph';
import CytoscapeComponent from 'react-cytoscapejs';

cytoscape.use(klay);


let id = 0
function insertNodeIntoTree(node, newNode) {
  if (node.hasOwnProperty('lineNum')) {
    newNode.data = {};
    newNode.data.fullMethod = node.method_full;
    newNode.data.lineNum = node.lineNum;
    if (node.children != null) {
      newNode.children = new Array(node.children.length);
      for (let i = 0; i < node.children.length; i++) {
        newNode.children[i] = {}
        insertNodeIntoTree(node.children[i], newNode.children[i]);
      }
    }
  } else {
    newNode.data = {};
    newNode.data.fullMethod = node.method_full;
  }
  id++;
  newNode.key = id;

  if (node.children != null) {
    newNode.children = new Array(node.children.length);
    for (let i = 0; i < node.children.length; i++) {
      newNode.children[i] = {}
      insertNodeIntoTree(node.children[i], newNode.children[i]);
    }
  }
}

const CallGraph = (props) => {
  const onSelect = (selectedKeys, info) => {
    console.log('selected', selectedKeys, info);
  };

  const titleRender = ({ title, key, data }) => {
    return <div>{data.lineNum}:{data.fullMethod} </div>;
  }

  const getcaller = async () => {
    const result = await getCallerGraph();
    var newNode = {};
    console.log(result);
    insertNodeIntoTree(result, newNode);
    console.log(newNode);
    setTreeData([newNode]);
  }

  const [treeData, setTreeData] = useState([]);
  const elements = [
    { data: { id: 'one', label: 'Node 1' }, position: { x: 0, y: 0 } },
    { data: { id: 'two', label: 'Node 2' }, position: { x: 100, y: 0 } },
    { data: { source: 'one', target: 'two', label: 'Edge from Node1 to Node2' } }
  ];
  return (

    <PageHeaderWrapper>


      <Tree
        treeData={treeData}
        onSelect={onSelect}
        titleRender={titleRender}
      />
      <CytoscapeComponent
        elements={elements}
        style={{ width: '2048px', height: '2048px' }}
        layout={{
          name: 'klay',
          zoom: 1,
          pan: {
            x: 100,
            y: 100
          },
          nodeDimensionsIncludeLabels: true,
          fit: true, // Whether to fit
          padding: 20, // Padding on fit
          animate: false, // Whether to transition the node positions
          animateFilter: function (node, i) { return true; }, // Whether to animate specific nodes when animation is on; non-animated nodes immediately go to their final positions
          animationDuration: 500, // Duration of animation in ms if enabled
          animationEasing: undefined, // Easing of animation if enabled
          transform: function (node, pos) { return pos; }, // A function that applies a transform to the final node position
          ready: undefined, // Callback on layoutready
          stop: undefined, // Callback on layoutstop
          klay: {
            // Following descriptions taken from http://layout.rtsys.informatik.uni-kiel.de:9444/Providedlayout.html?algorithm=de.cau.cs.kieler.klay.layered
            addUnnecessaryBendpoints: false, // Adds bend points even if an edge does not change direction.
            aspectRatio: 1.6, // The aimed aspect ratio of the drawing, that is the quotient of width by height
            borderSpacing: 20, // Minimal amount of space to be left to the border
            compactComponents: false, // Tries to further compact components (disconnected sub-graphs).
            crossingMinimization: 'LAYER_SWEEP', // Strategy for crossing minimization.
            /* LAYER_SWEEP The layer sweep algorithm iterates multiple times over the layers, trying to find node orderings that minimize the number of crossings. The algorithm uses randomization to increase the odds of finding a good result. To improve its results, consider increasing the Thoroughness option, which influences the number of iterations done. The Randomization seed also influences results.
            INTERACTIVE Orders the nodes of each layer by comparing their positions before the layout algorithm was started. The idea is that the relative order of nodes as it was before layout was applied is not changed. This of course requires valid positions for all nodes to have been set on the input graph before calling the layout algorithm. The interactive layer sweep algorithm uses the Interactive Reference Point option to determine which reference point of nodes are used to compare positions. */
            cycleBreaking: 'GREEDY', // Strategy for cycle breaking. Cycle breaking looks for cycles in the graph and determines which edges to reverse to break the cycles. Reversed edges will end up pointing to the opposite direction of regular edges (that is, reversed edges will point left if edges usually point right).
            /* GREEDY This algorithm reverses edges greedily. The algorithm tries to avoid edges that have the Priority property set.
            INTERACTIVE The interactive algorithm tries to reverse edges that already pointed leftwards in the input graph. This requires node and port coordinates to have been set to sensible values.*/
            direction: 'UNDEFINED', // Overall direction of edges: horizontal (right / left) or vertical (down / up)
            /* UNDEFINED, RIGHT, LEFT, DOWN, UP */
            edgeRouting: 'ORTHOGONAL', // Defines how edges are routed (POLYLINE, ORTHOGONAL, SPLINES)
            edgeSpacingFactor: 0.5, // Factor by which the object spacing is multiplied to arrive at the minimal spacing between edges.
            feedbackEdges: false, // Whether feedback edges should be highlighted by routing around the nodes.
            fixedAlignment: 'LEFTUP', // Tells the BK node placer to use a certain alignment instead of taking the optimal result.  This option should usually be left alone.
            /* NONE Chooses the smallest layout from the four possible candidates.
            LEFTUP Chooses the left-up candidate from the four possible candidates.
            RIGHTUP Chooses the right-up candidate from the four possible candidates.
            LEFTDOWN Chooses the left-down candidate from the four possible candidates.
            RIGHTDOWN Chooses the right-down candidate from the four possible candidates.
            BALANCED Creates a balanced layout from the four possible candidates. */
            inLayerSpacingFactor: 1.0, // Factor by which the usual spacing is multiplied to determine the in-layer spacing between objects.
            layoutHierarchy: false, // Whether the selected layouter should consider the full hierarchy
            linearSegmentsDeflectionDampening: 0.3, // Dampens the movement of nodes to keep the diagram from getting too large.
            mergeEdges: true, // Edges that have no ports are merged so they touch the connected nodes at the same points.
            mergeHierarchyCrossingEdges: false, // If hierarchical layout is active, hierarchy-crossing edges use as few hierarchical ports as possible.
            nodeLayering: 'NETWORK_SIMPLEX', // Strategy for node layering.
            /* NETWORK_SIMPLEX This algorithm tries to minimize the length of edges. This is the most computationally intensive algorithm. The number of iterations after which it aborts if it hasn't found a result yet can be set with the Maximal Iterations option.
            LONGEST_PATH A very simple algorithm that distributes nodes along their longest path to a sink node.
            INTERACTIVE Distributes the nodes into layers by comparing their positions before the layout algorithm was started. The idea is that the relative horizontal order of nodes as it was before layout was applied is not changed. This of course requires valid positions for all nodes to have been set on the input graph before calling the layout algorithm. The interactive node layering algorithm uses the Interactive Reference Point option to determine which reference point of nodes are used to compare positions. */
            nodePlacement: 'SIMPLE', // Strategy for Node Placement
            /* BRANDES_KOEPF Minimizes the number of edge bends at the expense of diagram size: diagrams drawn with this algorithm are usually higher than diagrams drawn with other algorithms.
            LINEAR_SEGMENTS Computes a balanced placement.
            INTERACTIVE Tries to keep the preset y coordinates of nodes from the original layout. For dummy nodes, a guess is made to infer their coordinates. Requires the other interactive phase implementations to have run as well.
            SIMPLE Minimizes the area at the expense of... well, pretty much everything else. */
            randomizationSeed: 2, // Seed used for pseudo-random number generators to control the layout algorithm; 0 means a new seed is generated
            routeSelfLoopInside: false, // Whether a self-loop is routed around or inside its node.
            separateConnectedComponents: true, // Whether each connected component should be processed separately
            spacing: 60, // Overall setting for the minimal amount of space to be left between objects
            thoroughness: 7 // How much effort should be spent to produce a nice layout..
          }
        }}
        stylesheet={[
          {
            selector: 'node',
            style: {
              width: 13,
              height: 13,
            }
          },
          {
            selector: 'edge',
            style: {
              curveStyle: 'bezier',
              targetArrowShape: 'triangle',
              curveStyle: "straight",
              width: 2
            }
          }
        ]}
      />;

    </PageHeaderWrapper>


  );
};

export default CallGraph;
