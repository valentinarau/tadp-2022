describe 'Operaciones' do
  Operaciones = Class.new do
    #precondición de dividir
    pre { divisor != 0 }
    #postcondición de dividir
    post { |result| result * divisor == dividendo }
    def dividir(dividendo, divisor)
      dividendo / divisor
    end
  end

  let(:operaciones) { Operaciones.new }

  it 'should fail pre condition ' do
    expect { operaciones.dividir(4, 0) }.to raise_error(PreBlockValidationError)
  end

  it 'should do the operation' do
    dividendo = 16
    divisor = 4
    expected_result = 4
    expect { operaciones.dividir(dividendo, divisor) }.not_to raise_error
    expect(operaciones.dividir(dividendo, divisor)).to eq expected_result
  end

end