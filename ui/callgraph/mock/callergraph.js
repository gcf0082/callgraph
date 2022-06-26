const treeData1 = `{
    "method_hash": "KoFZ6IDQYNLAUkvo5RicpQ#031",
    "method_full": "test.call_graph.method_call.TestMCCaller:test2c()",
    "children": [
        {
            "method_hash": "Db1pSyrspiE6RZ-lBNYLRg#024",
            "method_full": "java.lang.System:currentTimeMillis()",
            "lineNum": 78
        },
        {
            "method_hash": "Db1pSyrspiE6RZ-lBNYLRg#024",
            "method_full": "java.lang.System:currentTimeMillis()",
            "lineNum": 85
        },
        {
            "method_hash": "bcB3E0Ite2EG8fsL_A-eTQ#02e",
            "method_full": "test.call_graph.method_call.TestMCCaller:str()",
            "lineNum": 91
        },
        {
            "method_hash": "bcB3E0Ite2EG8fsL_A-eTQ#02e",
            "method_full": "test.call_graph.method_call.TestMCCaller:str()",
            "lineNum": 92
        },
        {
            "method_hash": "Pwm-cwAg_fNT-QOITHc-Wg#051",
            "method_full": "test.call_graph.method_call.TestMCCallee:test2(java.lang.String,java.lang.String)",
            "lineNum": 94,
            "children": [
                {
                    "method_hash": "gg6S9CkDUoo27Mrag3r4Pw#020",
                    "method_full": "java.io.PrintStream:println(int)",
                    "lineNum": 21
                },
                {
                    "method_hash": "SP_MOiuKbyMirmBMygzpGw#040",
                    "method_full": "test.call_graph.method_call.TestMCCallee:test1(java.lang.String)",
                    "lineNum": 22
                }
            ]
        }
    ]
}`;
export default {
    'GET /caller_graph': 
        JSON.parse(treeData1)      
}