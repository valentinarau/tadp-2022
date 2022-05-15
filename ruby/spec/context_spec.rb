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

  describe 'context delegates on instance' do
    let(:obj) { Case.new }
    let(:c) { Context.new(obj, []) }

    Case = Class.new do
      def return_4
        4
      end
      def return_param(val)
        val
      end
    end

    it 'should return 4' do
      expect(c.return_4).to be 4
    end

    it 'should return 111' do
      got = c.return_param(111)
      expect(got).to be 111
    end
  end
end