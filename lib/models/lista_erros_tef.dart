class ListaErrosTef {
  Map<int, String> mapErros =  { 
    1: "Endereço IP inválido ou não resolvido",
    2: "Código da loja inválido",
    3: "Código de terminal inválido",
    6: "Erro na inicialização do Tcp/Ip",
    7: "Falta de memória",
    8: "Não encontrou a CliSiTef ou ela está com problemas",
    9: "Configuração de servidores SiTef foi excedida",
    10: "Erro de acesso na pasta CliSiTef (possível falta de permissão para escrita)",
    11: "Dados inválidos passados pela automação.",
    12: "Modo seguro não ativo",
    13: "Caminho DLL inválido (o caminho completo das bibliotecas está muito grande).",
    -1: "Módulo não inicializado.",
    -2: "Operação cancelada pelo operador.",
    -3: "O parâmetro função / modalidade é inexistente/inválido.",
    -4: "Falta de memória no PDV.",
    -5: "Sem comunicação com o SiTef.",
    -6: "Operação cancelada pelo usuário (no pinpad).",
    -9: "A automação chamou a rotina ContinuaFuncaoSiTefInterativo sem antes iniciar uma função iterativa.",
    -10: "Algum parâmetro obrigatório não foi passado pela automação comercial.",
    -12: "Erro na execução da rotina iterativa. \n Provavelmente o processo iterativo anterior não foi executado até o final (enquanto o retorno for igual a 10000).",
    -13: "Documento fiscal não encontrado nos registros da CliSiTef. \n Retornado em funções de consulta tais como ObtemQuantidadeTransaçõesPendentes.",
    -15: "Operação cancelada pela automação comercial.",
    -20: "Parâmetro inválido passado para a função.",
    -25: "Erro no Correspondente Bancário: Deve realizar sangria.",
    -30: "Erro de acesso ao arquivo. \n Certifique-se que o usuário que roda a aplicação tem direitos de leitura/escrita.",
    -40: "Transação negada pelo servidor SiTef.",
    -41: "Dados inválidos.",
    -43: "Problema na execução de alguma das rotinas no pinpad.",
    -50: "Transação não segura.",
    -100: "Erro interno do módulo."
  };

  String mensagemErro(dynamic codresp){
    return mapErros[int.parse(codresp)]??"Erros detectados internamente pela rotina. \n Erro não classificado.";
  }
}