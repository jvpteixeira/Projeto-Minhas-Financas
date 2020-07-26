import React from 'react'

import {withRouter} from 'react-router-dom'
import Card from '../../components/card'
import FormGroup from '../../components/form-group'
import SelectMenu from '../../components/selectMenu'
import LancamentoTables from './lancamentosTable'
import LancamentoServices from '../../services/lancamentosService'
import LocalStorageService from '../../services/localStorageService'
import *  as messages from '../../components/toastr'
import {Dialog} from 'primereact/dialog'
import { Button } from 'primereact/button'

class ConsultaLancamentos extends React.Component {

    state={
        ano: '',
        mes: '',
        tipo: '',
        descricao : '',
        showConfirmDialog:false,
        lancamentoDeletar: {},
        lancamentos : []
    }

    constructor(){
        super();
        this.service = new LancamentoServices();
    }

    buscar = () => {
        if(!this.state.ano){
            messages.mensagemErro("O preenchimento do campo ano é obrigatório")
            return false;
        }
        const usuarioLogado = LocalStorageService.obterItem('_usuario_logado')
        const lancamentoFiltro = {
            ano: this.state.ano,
            mes:this.state.mes,
            tipo:this.state.tipo,
            usuario: usuarioLogado.id,
            descricao: this.state.descricao
        }
        this.service
            .consultarLancamentos(lancamentoFiltro)
            .then(response =>{
                console.log(this.state)
                this.setState({lancamentos:response.data})
            }).catch(error => {
                console.log(lancamentoFiltro)
            })
    }

    editar = (id) => {
       this.props.history.push(`/cadastro-lancamento/${id}`)
    }

    abrirConfirmacao = (lancamento) => {
        this.setState({showConfirmDialog: true, lancamentoDeletar:lancamento})
    }

    deletar = () => {
        this.service
            .deletar(this.state.lancamentoDeletar.id)
            .then(response => {
                this.setState({showConfirmDialog:false})
                const lancamentos = this.state.lancamentos
                const index = lancamentos.indexOf(this.state.lancamentoDeletar)
                lancamentos.splice(index,1);
                this.setState(lancamentos)
                messages.mensagemSucesso('Lançamento deletado com sucesso')
            }).catch(error => {
                messages.mensagemErro('Erro ao deletar lançancamento')
            })
    }

    cancelarDelecao = () => {
        this.setState({showConfirmDialog:false,lancamentoDeletar:{}})
    }

    preparaFormularioCadastro = () => {
        this.props.history.push('/cadastro-lancamento')
    }

    render(){
        const meses = this.service.obterListaMeses()

        const tipos =  this.service.obterListaTipos()

        const confirmDialogFooter = (
            <div>
               <Button label = "Confirmar" icon="pi pi-check" onClick={this.deletar}/> 
               <Button label = "Cancelar" icon="pi pi-check" onClick={this.cancelarDelecao} 
                    className="p-button-secondary"/> 
            </div>
        )
    
        return(
            <Card title='Consulta Lancamentos'>
                <div className="row">
                    <div className="col-md-6">
                        <div className="bs-component">
                            <FormGroup htmlFor="inputAno" label="Ano: *">
                                <input type="text" 
                                    className="form-control" 
                                    id="inputAno"
                                    placeholder="Digite o Ano"
                                    value={this.state.ano}
                                    onChange={e => this.setState({ano: e.target.value})}
                                />
                            </FormGroup>
                            <FormGroup htmlFor="inputMes" label="Mes:">
                                <SelectMenu id="inputMes" 
                                        className="form-control" 
                                        lista={meses}
                                        value={this.state.mes}
                                        onChange={e => this.setState({mes: e.target.value})}/>
                            </FormGroup>

                            <FormGroup htmlFor="inputDesc" label="Descrição:">
                                <input type="text" 
                                        className="form-control" 
                                        id="inputDesc"
                                        placeholder="Digite a descrição"
                                        value={this.state.descricao}
                                        onChange={e => this.setState({ descricao: e.target.value })}
                                    />
                            </FormGroup>

                            <FormGroup htmlFor="inputTipo" label="Tipo de Lançamentos:">
                                <SelectMenu id="inputTipo" 
                                        className="form-control" 
                                        lista={tipos}
                                        value={this.state.tipo}
                                        onChange={e => this.setState({tipo: e.target.value})}/>
                            </FormGroup>

                            <button type="button" className="btn btn-success" onClick={this.buscar}>Buscar</button>
                            <button onClick={this.preparaFormularioCadastro}type="button" className="btn btn-danger">Cadastrar</button>
                        </div>
                    </div>
                </div>
                <br/>
                <div className="row">
                    <div className="col-md-12">
                        <div className="bs-component">
                            <LancamentoTables lancamentos={this.state.lancamentos} 
                                deleteAction={this.abrirConfirmacao}
                                editAction={this.editar}/>
                        </div>
                        
                    </div>
                </div>
                <div>
                    <Dialog header="Confirmação" 
                        visible={this.state.showConfirmDialog} 
                        style={{width: '50vw'}} 
                        modal={true} 
                        onHide={() => this.setState({showConfirmDialog: false})}
                        footer={confirmDialogFooter}>
                        Confirma exclusão deste Lançamento?
                    </Dialog>
                </div>

            </Card>
        )
    }
}

export default withRouter(ConsultaLancamentos)
