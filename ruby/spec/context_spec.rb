require_relative '../lib/context'

describe Context do
  it 'should instantiate a context' do
    Context.new(self, [], nil)
  end

  describe 'set context' do
    parameters = [
      [:req, :method1],
      [:req, :method2],
    ]


    it 'should define a method for each parameter' do
      c = Context.new(self, parameters, nil)
      expect(c.methods.include?(:sarasa)).to be false
      expect(c.methods.include?(:method1)).to be true
      expect(c.methods.include?(:method2)).to be true
    end

    it 'should define a method that returns args' do
      args = [11, 22]
      c = Context.new(self, parameters, *args)
      expect(c.method1).to eq(11)
      expect(c.method2).to eq(22)
    end

    it 'should not throw exception when args has not value' do
      c = Context.new(self, parameters)
      expect(c.method1).to be nil
    end
  end

end