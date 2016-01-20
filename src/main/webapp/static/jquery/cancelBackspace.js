$(document).keydown(function(e){   
        var keyEvent;   
        if(e.keyCode==8){   
            var d=e.srcElement||e.target;   
             if(d.tagName.toUpperCase()=='INPUT'||d.tagName.toUpperCase()=='TEXTAREA'){   
                 keyEvent=d.readOnly||d.disabled;   
             }else{   
                 keyEvent=true;   
             }   
         }else{   
             keyEvent=false;   
         }   
         if(keyEvent){   
             e.preventDefault();   
         }   
 }); 