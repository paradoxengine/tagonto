 <?php
 /**
 * This class is meant to manage the configuration file of TagontoLib
 **/
class TagontoConfiguration
{
  private $configFile;

  private $items = array();

  function __construct($configfile) { 
  	$this->configFile = $configfile;
  	$this->parse(); 
  }

  function __get($id) { return $this->items[ $id ]; }

  function parse()
  {
    $fh = fopen( $this->configFile, 'r' );
    while( $l = fgets( $fh ) )
    {
      if ( preg_match( '/^#/', $l ) == false )
      {
        preg_match( '/^(.*?)=(.*?)$/', $l, $found );
        $this->items[ $found[1] ] = $found[2];
      }
    }
    fclose( $fh );
  }
  
 function __set($id,$v) { 
 	$this->items[ $id ] = $v; 
 }
 
  function save()
  {
    $nf = '';
    $fh = fopen( $this->configFile, 'r' );
    while( $l = fgets( $fh ) )
    {
      if ( preg_match( '/^#/', $l ) == false )
      {
        preg_match( '/^(.*?)=(.*?)$/', $l, $found );
        $nf .= $found[1]."=".$this->items[$found[1]]."\n";
      }
      else
      {
        $nf .= $l;
      }
    }
    fclose( $fh );
   // copy( $this->configFile, $this->configFile.'.bak' );
    $fh = fopen( $this->configFile, 'w' );
    fwrite( $fh, $nf );
    fclose( $fh );
  }
 
}

?>
