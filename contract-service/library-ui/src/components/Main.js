import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import contractService from '../services/ContractService';
import React, { useState, useRef } from 'react';
import Table from 'react-bootstrap/Table';
import Alert from 'react-bootstrap/Alert';
import Tooltip from 'react-bootstrap/Tooltip';
import Overlay from 'react-bootstrap/Overlay';


const delay = ms => new Promise(
  resolve => setTimeout(resolve, ms)
);

function Main() {

  const [show, setShow] = useState(false);
  const target = useRef(null);


  const [contract, setContract] = useState({
    integrationId: '',
    contractNumber: ''
  });

  const [statuses, setStatuses] = useState([]);

  const addStatusListRow = (cs) => {

    setStatuses([...statuses, cs])
  }

  async function startHandle() {
    setContract({
      ...contract,
      integrationId: ''
    });
    if (contract.contractNumber === '') {
      setShow(true);
      return;
    }
    setShow(false);
    const cnt = await contractService.passToHandling(contract);
    handleIntegrationIdChange(cnt);
    console.log(cnt);

    var status = 'PENDING';

    do {
      await delay(1000);
      const s = await contractService.getStatus(cnt.integrationId);
      try {
        if ('error' in s) {
          continue;
        }
      } catch (e) { }
      var variant = (s !== 'CANCELED' && s !== 'INCORRECT') ? "success" : "danger";
      addStatusListRow({ "contractId": cnt.integrationId, "status": s, "variant": variant });
      status = s;
    } while (status !== 'CANCELED' && status !== 'CONFIRMED');

  }

  function handleNumberChange(e) {
    setContract({
      ...contract,
      contractNumber: e.target.value
    });
  }

  const statusList = statuses.map(status => {
    return <tr key={status.id}>
      <td >{status.contractId}</td>
      <td >
        <Alert key={status.variant} variant={status.variant}>
          {status.status}
        </Alert></td>
    </tr>
  });

  function handleIntegrationIdChange(e) {
    setContract({
      ...contract,
      integrationId: e.integrationId
    });
  }

  return (
    <div className="container" id="main" >

      <form id="main-form" className="card mb-auto " onSubmit={startHandle}>
        <Card >
          <h3 className="title fw-bold text-success shadow-none text-center" >ОБРАБОТОТКА ДОГОВОРА</h3>
          <Card.Body className="card card-body p-3 bg-light">
            <div className="form-edit-group-row">
              <Button variant="primary" type="button"
                onClick={startHandle}
              >Начать обработку</Button>
            </div>
            <Form.Group className="form-edit-group-row mb-3"  >
              <Form.Label>ID обработки</Form.Label>
              <Form.Control type="text" name="integrationId" id="id" value={contract.integrationId} disabled readOnly />
            </Form.Group>
            <Form.Group className="form-edit-group-row mb-3" >
              <Form.Label>Номер договора</Form.Label>


                <Form.Control type="text" name="contractNumber" ref={target} placeholder="Номер договора" onChange={handleNumberChange} value={contract.contractNumber} required />

              <Overlay target={target.current} show={show} placement="bottom">
                {({
                  placement: _placement,
                  arrowProps: _arrowProps,
                  show: _show,
                  popper: _popper,
                  hasDoneInitialMeasure: _hasDoneInitialMeasure,
                  ...props
                }) => (
                  <div
                    {...props}
                    style={{
                      position: 'absolute',
                      backgroundColor: 'rgba(255, 100, 100, 0.85)',
                      padding: '2px 10px',
                      color: 'white',
                      borderRadius: 3,
                      ...props.style,
                    }}
                  >
                    Введите номер договора
                  </div>
                )}
              </Overlay>



            </Form.Group>
          </Card.Body>
        </Card>
      </form>
      <Table className="table" bordered striped hover>
        {/* <thead className="table-light"> */}
        <thead className="table">
          <tr>
            <th >id</th>
            <th >СТАТУС</th>
          </tr>
        </thead>
        <tbody className="table-group-divider">
          {statusList}
        </tbody>
      </Table>
    </div>
  );
}

export default Main;