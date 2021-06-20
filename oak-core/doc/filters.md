# Filters

## Flow

1. Routing
    
    Check path, get corresponding endpoint
   
2. Security
    
    Check security, apply session
    
3. Preprocessor
    
    Get file (stream), transform to json 
    
4. EndPoint
    
    Call
    
5. Decoration
    
    Apply decorations (toJson, gzip, headers, etc)
    
   