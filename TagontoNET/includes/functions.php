<?php

function dom_to_simple_array($domnode, &$array) {
  $array_ptr = &$array;
  $domnode = $domnode->firstChild;
  while (!is_null($domnode)) {
    if (! (trim($domnode->nodeValue) == "") ) {
      switch ($domnode->nodeType) {
        case XML_TEXT_NODE: {
          $array_ptr['cdata'] = $domnode->nodeValue;
          break;
        }
        case XML_ELEMENT_NODE: {
          $array_ptr = &$array[$domnode->nodeName][];
          if ($domnode->hasAttributes() ) {
            $attributes = $domnode->attributes;
            if (!is_array ($attributes)) {
              break;
            }
            foreach ($attributes as $index => $domobj) {
              $array_ptr[$index] = $array_ptr[$domobj->name] = $domobj->value;
            }
          }
          break;
        }
      }
      if ( $domnode->hasChildNodes() ) {
        dom_to_simple_array($domnode, $array_ptr);
      }
    }
    $domnode = $domnode->nextSibling;
  }
}
?>