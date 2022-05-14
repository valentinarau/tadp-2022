require_relative '../lib/persona'

describe Contrato do
  pre_edad_block = false
  let(:persona) { Persona.new }

  before(:each) do
    pre_edad_block = false
  end

  Persona = Class.new do
    # invariant { !@edad.nil? && @edad > 0 }
    # invariant { false }
    invariant { true }

    pre { pre_edad_block = true }
    post { "".nil? }
    def edad
      puts 'la edad es ' + @edad.to_s
      @edad
    end

    def set_edad(n)
      puts 'seteando edad en ' + n.to_s
      @edad = n
    end

    pre { "".nil? }
    post { "".nil? }

    def another_method
      puts "ejemplo"
    end
  end

  it 'should execute edad pre-block' do
    persona.edad
    expect(pre_edad_block).to be true
  end

end