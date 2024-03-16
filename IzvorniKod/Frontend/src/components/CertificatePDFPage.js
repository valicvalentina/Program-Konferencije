import Document from './CertificateDocument'
import { Link } from 'react-router-dom';
import {Button} from 'semantic-ui-react';

function CertificatePDFPage() {
  return (
    <>
    <div>
        <Link to="/myConference"><Button color='teal' style={{width: 220, margin:10}}>MY CONFERENCE</Button></Link>
    </div>
    <div >
      <Document />
    </div>
    </>
    
  );
}
export default CertificatePDFPage;