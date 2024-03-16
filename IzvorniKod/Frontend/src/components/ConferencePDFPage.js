import Document from './ConferenceDocument'
import { Link } from 'react-router-dom';
import {Button} from 'semantic-ui-react';

function ConferencePDFPage() {
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
export default ConferencePDFPage;