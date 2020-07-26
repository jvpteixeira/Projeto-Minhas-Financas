import ApiService from '../app/apiservice'
import ErroValidacao from '../app/exception/ErroValidação'

export default class UsuarioService extends ApiService{
    constructor(){
        super('/api/usuarios')
    }

    autenticar(credenciais){
        return this.post('/autenticar',credenciais)
    }

    obterSaldoPorUsuario(id){
        return this.get(`/${id}/saldo`)
    }

    salvarUsuario(usuario){
        return this.post("/",usuario);
    }
    
    validar(usuario){
        const erros = []
        
        if(!usuario.nome){
            erros.push('O campo nome é obrigatório')
        }

        if(!usuario.email){
            erros.push('O campo email é obrigatório')
        } else if(!usuario.email.match(/^[a-z0-9.]+@[a-z0-9]+\.[a-z]/) ){
            erros.push('Informe um email válido')
        }

        if(!usuario.senha){
            erros.push('O campo senha é obrigatório')
        } else if (usuario.senha !== usuario.senhaRepeticao){
            erros.push('Campo senha está diferente da repetição')
        }

        if(erros && erros.length >0){
            throw new ErroValidacao(erros)
        }

        return erros;
    }
}

